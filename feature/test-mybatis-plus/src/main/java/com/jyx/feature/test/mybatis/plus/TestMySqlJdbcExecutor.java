package com.jyx.feature.test.mybatis.plus;

import com.jyx.infra.mybatis.plus.DbHolder;
import com.jyx.infra.mybatis.plus.jdbc.common.CursorPreparedStatementCreator;
import com.jyx.infra.mybatis.plus.jdbc.common.JdbcHelper;
import com.jyx.infra.mybatis.plus.jdbc.common.ObjectArrayRowMapper;
import com.jyx.infra.mybatis.plus.query.PageHelper;
import com.jyx.infra.spring.context.AppConstant;
import com.jyx.infra.thread.FutureResult;
import com.jyx.infra.util.ConstructorUtil;
import com.jyx.infra.util.ListUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author Archforce
 * @since 2023/11/21 11:24
 */
@Component
public class TestMySqlJdbcExecutor {


    private final DbHolder dbHolder;


    private final ThreadPoolExecutor ioPool;


    private final ThreadPoolExecutor calculatePool;

    public TestMySqlJdbcExecutor(DbHolder dbHolder,
                                 @Qualifier(AppConstant.IO_POOL_NAME) ThreadPoolExecutor ioPool,
                                 @Qualifier(AppConstant.CALCULATE_POOL_NAME) ThreadPoolExecutor calculatePool) {
        this.dbHolder = dbHolder;
        this.ioPool = ioPool;
        this.calculatePool = calculatePool;
    }


    public <T> long batchQueryAll(Class<T> clazz, String select, String where, int taskSize, int batchSize) {
        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(clazz);
        String tableName = dbHolder.tableName(clazz);

        if (select == null || select.isEmpty()) {
            select = " * ";
        }
        StringBuilder queryByIdRangeBuilder = new StringBuilder("SELECT ")
                .append(select)
                .append(" FROM ")
                .append(tableName)
                .append(" " + where);
        String sql = queryByIdRangeBuilder.toString();

        Constructor<T> mostArgConstructor = ConstructorUtil.findMostArgConstructor(clazz);

        LongAdder longAdder = new LongAdder();

        PreparedStatementCreator psc = new CursorPreparedStatementCreator(sql, batchSize);
        ObjectArrayRowMapper objectArrayRowMapper = new ObjectArrayRowMapper();
        RowMapper<Object> objectRowMapper = (rs, rowNum) -> {
            Object[] objects = objectArrayRowMapper.mapRow(rs, rowNum);
            T instance = ConstructorUtil.newInstance(mostArgConstructor, objects);
            longAdder.add(instance != null ? 1 : 0);
            instance = null;
            return null;
        };
        RowMapperResultSetExtractor resultSetExtractor = new RowMapperResultSetExtractor<>(objectRowMapper);
        jdbcTemplate.query(psc, resultSetExtractor);
        return longAdder.sum();
    }

    public <T> long batchQueryAllAsync(Class<T> clazz, String select, String where, int taskSize, int batchSize) {

        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(clazz);
        String tableName = dbHolder.tableName(clazz);
        Constructor<T> mostArgConstructor = ConstructorUtil.findMostArgConstructor(clazz);

        Integer totalCount = count(jdbcTemplate, tableName, where);
        List<Long> startIdList = startIdEveryWorker(jdbcTemplate, tableName, where, totalCount, taskSize);
        return queryByStartId(jdbcTemplate, mostArgConstructor,
                tableName, select, where,
                startIdList,
                taskSize, batchSize);
    }

    private Integer count(JdbcTemplate jdbcTemplate,
                          String tableName, String where) {
        StringBuilder countSqlBuilder = new StringBuilder("SELECT COUNT(id) FROM ").append(tableName).append(" ");

        if (where != null && !where.isEmpty()) {
            countSqlBuilder.append(where);
        }
        return jdbcTemplate.queryForObject(countSqlBuilder.toString(), Integer.class);
    }

    private List<Long> startIdEveryWorker(JdbcTemplate jdbcTemplate,
                                          String tableName, String where,
                                          int totalCount, int taskSize) {
        int workerSize = PageHelper.calculateNumberOfPage(totalCount, taskSize);
        String varSql = "SET @last_max_id=0;";
        StringBuilder startIdEveryWorkerSqlBuilder = new StringBuilder("SELECT 0 ");
        if (where == null || where.isEmpty()) {
            where = " WHERE ";
        } else {
            where = " " + where + " AND ";
        }
        String oneIdSql = String.format(" SELECT max_id FROM (SELECT @last_max_id:=max(id) as max_id FROM (SELECT id FROM %s %s id > (SELECT @last_max_id) limit %s) tmp) t1 ", tableName, where, taskSize);
        for (int i = 0; i < workerSize; i++) {
            startIdEveryWorkerSqlBuilder.append(" UNION ALL ")
                    .append(oneIdSql);
        }
        String startIdEveryWorkerSql = startIdEveryWorkerSqlBuilder.toString();

        List<Long> startIdList = jdbcTemplate.execute(
                (ConnectionCallback<List<Long>>) con -> {
                    Statement stmt = null;
                    ResultSet rs = null;
                    try {
                        stmt = con.createStatement();
                        stmt.execute(varSql);
                        rs = stmt.executeQuery(startIdEveryWorkerSql);
                        JdbcHelper.handleWarnings(stmt, jdbcTemplate.isIgnoreWarnings());

                        RowMapperResultSetExtractor<Long> rse = new RowMapperResultSetExtractor<>(new SingleColumnRowMapper<>(Long.class));
                        return rse.extractData(rs);
                    } catch (SQLException ex) {
                        String sql = varSql + startIdEveryWorkerSql;

                        String task = "StatementCallback MySqlJdbcExecutor#startIdEveryWorker";
                        DataAccessException dae = jdbcTemplate.getExceptionTranslator().translate(task, sql, ex);
                        throw (dae != null ? dae : new UncategorizedSQLException(task, sql, ex));
                    } finally {
                        JdbcUtils.closeResultSet(rs);
                        JdbcUtils.closeStatement(stmt);
                        rs = null;
                        stmt = null;
                    }
                }
        );

        return startIdList;
    }

    private long queryByStartId(JdbcTemplate jdbcTemplate, Constructor<?> mostArgConstructor,
                                String tableName, String select, String where,
                                List<Long> startIdList,
                                int taskSize, int batchSize) {

        LongAdder longAdder = new LongAdder();

        ObjectArrayRowMapper objectArrayRowMapper = new ObjectArrayRowMapper();
        RowMapper<Object> objectRowMapper = (rs, rowNum) -> {
            Object[] objects = objectArrayRowMapper.mapRow(rs, rowNum);
            Object instance = ConstructorUtil.newInstance(mostArgConstructor, objects);
            longAdder.add(instance != null ? 1 : 0);
            instance = null;
            return null;
        };
        RowMapperResultSetExtractor resultSetExtractor = new RowMapperResultSetExtractor<>(objectRowMapper);

        List<CompletableFuture<Void>> rangeFutureList = new ArrayList<>(startIdList.size());
        final String queryByIdRangeSql = buildQueryByIdRangeSql(tableName, select, where);
        for (int startIdIndex = 0; startIdIndex < startIdList.size() - 1; startIdIndex++) {
            Long lowId = startIdList.get(startIdIndex);
            Long highId = startIdList.get(startIdIndex + 1);

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                PreparedStatementCreator psc = new CursorPreparedStatementCreator(queryByIdRangeSql, batchSize);
                ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(new Object[]{lowId, highId});
                jdbcTemplate.query(psc, pss, resultSetExtractor);
            }, ioPool);
            rangeFutureList.add(future);
        }
        FutureResult.mergeCompletableFuture(rangeFutureList);
        return longAdder.sum();
    }

    private <T> List<T> mergeFuture(List<CompletableFuture<List<T>>> futureList) {
        List<List<T>> twoDimensionalList = FutureResult.mergeCompletableFuture(futureList);
        List<T> result = ListUtil.flatList(twoDimensionalList);
        return result;
    }

    private String buildQueryByIdRangeSql(String tableName, String select, String where) {
        if (select == null || select.isEmpty()) {
            select = " * ";
        }
        StringBuilder queryByIdRangeBuilder = new StringBuilder("SELECT ")
                .append(select)
                .append(" FROM ")
                .append(tableName);
        if (where == null || where.isEmpty()) {
            where = " WHERE ";
        } else {
            where = " " + where + " AND ";
        }
        queryByIdRangeBuilder.append(where)
                .append(" id > ? ")
                .append(" AND id <= ? ");
        return queryByIdRangeBuilder.toString();
    }

}
