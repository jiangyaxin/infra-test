package com.jyx.infra.spring.jdbc.mysql.reader;

import com.jyx.infra.collection.Tuple2;
import com.jyx.infra.collection.Tuples;
import com.jyx.infra.constant.StringConstant;
import com.jyx.infra.spring.jdbc.JdbcHelper;
import com.jyx.infra.spring.jdbc.reader.JdbcReader;
import com.jyx.infra.thread.FutureResult;
import com.jyx.infra.util.PageUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static com.jyx.infra.spring.jdbc.mysql.reader.AbstractMysqlJdbcReader.Constants.*;

/**
 * @author jiangyaxin
 * @since 2023/11/24 13:57
 */
public abstract class AbstractMysqlJdbcReader<T> implements JdbcReader<T> {

    interface Constants {
        String ALL_SELECT = "*";

        String WHERE = "WHERE";

        String UNION_ALL = "UNION ALL";

        String COUNT_SQL_TEMPLATE = "SELECT COUNT(id) FROM %s %s";

        String START_ID_OF_EACH_WORKER_SQL_1 = "SET @last_max_id=0;";
        String START_ID_OF_EACH_WORKER_SQL_2 = "SELECT 0";
        String START_ID_OF_EACH_WORKER_SQL_3 = "SELECT max_id FROM (SELECT @last_max_id:=max(id) as max_id FROM (SELECT id FROM %s %s id > (SELECT @last_max_id) limit %s) tmp) t1 ";

        String ID_RANGE_SQL_TEMPLATE = "SELECT %s FROM %s %s  id > ?  AND id <= ?";
    }

    private final ThreadPoolExecutor ioPool;

    public AbstractMysqlJdbcReader(ThreadPoolExecutor ioPool) {
        this.ioPool = ioPool;
    }

    public List<CompletableFuture<T>> batchQueryAsync(JdbcTemplate jdbcTemplate,
                                                      String tableName, String select, String where, Object[] args,
                                                      int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        Integer totalCount = count(jdbcTemplate,
                tableName, where, args);
        List<Long> startIdOfEachWorkerList = queryStartIdOfEachWorker(jdbcTemplate,
                tableName, where, args,
                totalCount, taskSizeOfEachWorker);

        List<CompletableFuture<T>> futureList = new ArrayList<>(startIdOfEachWorkerList.size());
        String queryByIdRangeSql = buildQueryByIdRangeSql(tableName, select, where);
        for (int startIdIndex = 0; startIdIndex < startIdOfEachWorkerList.size() - 1; startIdIndex++) {
            Long lowId = startIdOfEachWorkerList.get(startIdIndex);
            Long highId = startIdOfEachWorkerList.get(startIdIndex + 1);

            Object[] actualArgs = buildQueryByIdRangeArgs(args, lowId, highId);
            Tuple2<String, Object[]> sqlPair = Tuples.of(queryByIdRangeSql, actualArgs);

            CompletableFuture<T> future = CompletableFuture.supplyAsync(() -> queryByIdRange(jdbcTemplate, sqlPair, onceBatchSizeOfEachWorker), ioPool);

            futureList.add(future);
        }
        return futureList;
    }

    protected abstract T queryByIdRange(JdbcTemplate jdbcTemplate, Tuple2<String, Object[]> sqlPair, int onceBatchSizeOfEachWorker);

    private Integer count(JdbcTemplate jdbcTemplate,
                          String tableName, String where, Object[] args) {
        String countSql = buildCountSql(tableName, where);
        return jdbcTemplate.queryForObject(countSql, args, Integer.class);
    }

    private List<Long> queryStartIdOfEachWorker(JdbcTemplate jdbcTemplate,
                                                String tableName, String where, Object[] args,
                                                int totalCount, int taskSizeOfEachWorker) {
        int workerSize = PageUtil.calculateNumberOfPage(totalCount, taskSizeOfEachWorker);
        String varSql = START_ID_OF_EACH_WORKER_SQL_1;
        Tuple2<String, Object[]> sqlPair = buildStartIdOfEachWorkerSql(tableName, where, args, workerSize, taskSizeOfEachWorker);
        String startIdOfEachWorkerSql = sqlPair.getKey();
        ArgumentPreparedStatementSetter preparedStatementSetter = new ArgumentPreparedStatementSetter(sqlPair.getValue());

        List<Long> startIdList = jdbcTemplate.execute(
                (ConnectionCallback<List<Long>>) con -> {
                    Statement stmt = null;
                    PreparedStatement pstmt = null;
                    ResultSet rs = null;
                    try {
                        stmt = con.createStatement();
                        stmt.execute(varSql);

                        pstmt = con.prepareStatement(startIdOfEachWorkerSql);
                        preparedStatementSetter.setValues(pstmt);
                        rs = pstmt.executeQuery();
                        JdbcHelper.handleWarnings(stmt, jdbcTemplate.isIgnoreWarnings());

                        RowMapperResultSetExtractor<Long> rse = new RowMapperResultSetExtractor<>(new SingleColumnRowMapper<>(Long.class));
                        return rse.extractData(rs);
                    } catch (SQLException ex) {
                        String sql = varSql + startIdOfEachWorkerSql;

                        String task = String.format("StatementCallback %s#queryStartIdOfEachWorker", this.getClass().getName());
                        DataAccessException dae = jdbcTemplate.getExceptionTranslator().translate(task, sql, ex);
                        throw (dae != null ? dae : new UncategorizedSQLException(task, sql, ex));
                    } finally {
                        JdbcUtils.closeResultSet(rs);
                        JdbcUtils.closeStatement(stmt);
                        JdbcUtils.closeStatement(pstmt);
                        rs = null;
                        stmt = null;
                        pstmt = null;
                    }
                }
        );

        return startIdList;
    }

    private String buildQueryByIdRangeSql(String tableName, String select, String where) {
        String tmpSelect = formatSelect(select);
        String tmpWhere = formatWhere(where);

        String idRangeSql = String.format(ID_RANGE_SQL_TEMPLATE, tmpSelect, tableName, tmpWhere);
        return idRangeSql;
    }

    private Object[] buildQueryByIdRangeArgs(Object[] args, Long lowId, Long highId) {
        Object[] actualArgs = new Object[args.length + 2];
        System.arraycopy(args, 0, actualArgs, 0, args.length);
        actualArgs[args.length] = lowId;
        actualArgs[args.length + 1] = highId;
        return actualArgs;
    }


    private Tuple2<String, Object[]> buildStartIdOfEachWorkerSql(String tableName, String where, Object[] args, int workerSize, int taskSizeOfEachWorker) {
        String tmpWhere = formatWhere(where);
        StringBuilder startIdEachWorkerSqlBuilder = new StringBuilder(START_ID_OF_EACH_WORKER_SQL_2).append(StringConstant.EMPTY);
        String oneIdSql = String.format(START_ID_OF_EACH_WORKER_SQL_3, tableName, tmpWhere, taskSizeOfEachWorker);
        int argLength = args.length;
        Object[] actualArgs = new Object[argLength * workerSize];
        for (int i = 0; i < workerSize; i++) {
            startIdEachWorkerSqlBuilder.append(StringConstant.EMPTY).append(UNION_ALL).append(StringConstant.EMPTY)
                    .append(oneIdSql);
            System.arraycopy(args, 0, actualArgs, i * argLength, argLength);
        }
        return Tuples.of(startIdEachWorkerSqlBuilder.toString(), actualArgs);
    }


    private String buildCountSql(String tableName, String where) {
        String tmpWhere = formatWhere(where);
        String countSql = String.format(COUNT_SQL_TEMPLATE, tableName, tmpWhere);
        return countSql;
    }

    protected String formatWhere(String where) {
        if (where == null || where.trim().isEmpty()) {
            return "";
        }
        String tmpWhere = where.trim().toUpperCase();
        if (tmpWhere.startsWith(WHERE)) {
            return where;
        } else {
            return WHERE + StringConstant.EMPTY + where;
        }
    }

    protected String formatSelect(String select) {
        if (select == null || select.trim().isEmpty()) {
            return StringConstant.EMPTY + ALL_SELECT + StringConstant.EMPTY;
        }
        return StringConstant.EMPTY + select + StringConstant.EMPTY;
    }
}
