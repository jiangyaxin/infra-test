package com.jyx.infra.mybatis.plus.jdbc.mysql;

import com.jyx.infra.mybatis.plus.DbHolder;
import com.jyx.infra.mybatis.plus.jdbc.JdbcExecutor;
import com.jyx.infra.mybatis.plus.jdbc.common.*;
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

/**
 * @author Archforce
 * @since 2023/11/21 11:24
 */
@Component(MySqlJdbcExecutor.Constants.SERVICE_NAME)
public class MySqlJdbcExecutor implements JdbcExecutor {

    public interface Constants {
        String SERVICE_NAME = "mySqlJdbcExecutor";
    }

    private final DbHolder dbHolder;


    private final ThreadPoolExecutor ioPool;


    private final ThreadPoolExecutor calculatePool;

    public MySqlJdbcExecutor(DbHolder dbHolder,
                             @Qualifier(AppConstant.IO_POOL_NAME) ThreadPoolExecutor ioPool,
                             @Qualifier(AppConstant.CALCULATE_POOL_NAME) ThreadPoolExecutor calculatePool) {
        this.dbHolder = dbHolder;
        this.ioPool = ioPool;
        this.calculatePool = calculatePool;
    }

    @Override
    public <T> void batchInsert(List<T> dataList) {

    }

    @Override
    public <T, OUT> List<OUT> batchQueryAll(Class<T> clazz, String where,
                                            int taskSize, int batchSize) {
        List<CompletableFuture<List<OUT>>> futureList = batchQueryAllAsync(clazz, where, taskSize, batchSize);
        return mergeFuture(futureList);
    }

    @Override
    public <T, OUT> List<OUT> batchQueryAll(Class<T> clazz, Constructor<T> constructor,
                                            String select, String where,
                                            int taskSize, int batchSize) {
        List<CompletableFuture<List<OUT>>> futureList = batchQueryAllAsync(clazz, constructor, select, where, taskSize, batchSize);
        return mergeFuture(futureList);
    }

    @Override
    public <T, OUT> List<OUT> batchQueryAllAndProcess(Class<T> clazz, String where, ResultSetExtractPostProcessor<T, OUT> instancePostProcessor, int taskSize, int batchSize) {
        List<CompletableFuture<List<OUT>>> futureList = batchQueryAllAndProcessAsync(clazz, where, instancePostProcessor, taskSize, batchSize);
        return mergeFuture(futureList);
    }

    @Override
    public <T, OUT> List<OUT> batchQueryAllAndProcess(Class<T> clazz, Constructor<T> constructor, String select, String where, ResultSetExtractPostProcessor<T, OUT> instancePostProcessor, int taskSize, int batchSize) {
        List<CompletableFuture<List<OUT>>> futureList = batchQueryAllAndProcessAsync(clazz, constructor, select, where, instancePostProcessor, taskSize, batchSize);
        return mergeFuture(futureList);
    }

    @Override
    public <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAsync(Class<T> clazz, String where,
                                                                          int taskSize, int batchSize) {
        Constructor<T> mostArgConstructor = ConstructorUtil.findMostArgConstructor(clazz);
        return batchQueryAllAsync(clazz, mostArgConstructor,
                "*", where,
                taskSize, batchSize);
    }

    @Override
    public <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAsync(Class<T> clazz, Constructor<T> constructor,
                                                                          String select, String where,
                                                                          int taskSize, int batchSize) {
        InstancePostProcessorResultSet<T, OUT> extractPostProcessor = new InstancePostProcessorResultSet<>(constructor);
        return batchQueryAllNotInstanceAsync(clazz,
                select, where,
                extractPostProcessor,
                taskSize, batchSize);
    }

    @Override
    public <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAndProcessAsync(Class<T> clazz, String where,
                                                                                    ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                                                                    int taskSize, int batchSize) {
        Constructor<T> mostArgConstructor = ConstructorUtil.findMostArgConstructor(clazz);
        return batchQueryAllAndProcessAsync(clazz, mostArgConstructor,
                "*", where,
                instancePostProcessor,
                taskSize, batchSize);
    }

    @Override
    public <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAndProcessAsync(Class<T> clazz, Constructor<T> constructor,
                                                                                    String select, String where,
                                                                                    ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                                                                    int taskSize, int batchSize) {

        InstancePostProcessorResultSet<T, OUT> extractPostProcessor = new InstancePostProcessorResultSet<>(constructor, instancePostProcessor, true);
        return batchQueryAllNotInstanceAsync(clazz,
                select, where,
                extractPostProcessor,
                taskSize, batchSize);
    }

    @Override
    public <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllNotInstanceAsync(Class<T> clazz,
                                                                                     String select, String where,
                                                                                     ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor,
                                                                                     int taskSize, int batchSize) {
        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(clazz);
        String tableName = dbHolder.tableName(clazz);

        Integer totalCount = count(jdbcTemplate, tableName, where);
        List<Long> startIdList = startIdEveryWorker(jdbcTemplate, tableName, where, totalCount, taskSize);
        return queryByStartId(jdbcTemplate,
                tableName, select, where,
                startIdList,
                extractPostProcessor,
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

    private <OUT> List<CompletableFuture<List<OUT>>> queryByStartId(JdbcTemplate jdbcTemplate,
                                                                    String tableName, String select, String where,
                                                                    List<Long> startIdList,
                                                                    ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor,
                                                                    int taskSize, int batchSize) {

        List<CompletableFuture<List<CompletableFuture<List<OUT>>>>> rangeFutureList = new ArrayList<>(startIdList.size());
        final String queryByIdRangeSql = buildQueryByIdRangeSql(tableName, select, where);
        for (int startIdIndex = 0; startIdIndex < startIdList.size() - 1; startIdIndex++) {
            Long lowId = startIdList.get(startIdIndex);
            Long highId = startIdList.get(startIdIndex + 1);

            CompletableFuture<List<CompletableFuture<List<OUT>>>> future = CompletableFuture.supplyAsync(() -> queryByIdRange(jdbcTemplate,
                    queryByIdRangeSql, lowId, highId,
                    extractPostProcessor,
                    taskSize, batchSize), ioPool);

            rangeFutureList.add(future);
        }
        List<List<CompletableFuture<List<OUT>>>> twoDimensionalList = FutureResult.mergeCompletableFuture(rangeFutureList);
        List<CompletableFuture<List<OUT>>> futureList = ListUtil.flatList(twoDimensionalList);
        return futureList;
    }

    private <OUT> List<CompletableFuture<List<OUT>>> queryByIdRange(JdbcTemplate jdbcTemplate,
                                                                    String queryByIdRangeSql, Long lowId, Long highId,
                                                                    ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor,
                                                                    int taskSize, int batchSize) {
        PreparedStatementCreator psc = new CursorPreparedStatementCreator(queryByIdRangeSql, batchSize);
        ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(new Object[]{lowId, highId});

        int taskExpected = PageHelper.calculateNumberOfPage(taskSize, batchSize);
        AsyncResultSetExtractor<OUT> resultSetExtractor = new AsyncResultSetExtractor<>(extractPostProcessor, taskExpected, batchSize, calculatePool);
        List<CompletableFuture<List<OUT>>> futureList = jdbcTemplate.query(psc, pss, resultSetExtractor);
        return futureList;
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
