package com.jyx.infra.mybatis.plus.jdbc;

import com.jyx.infra.mybatis.plus.DbHolder;
import com.jyx.infra.spring.context.AppConstant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Archforce
 * @since 2023/11/21 11:24
 */
@Component
@AllArgsConstructor
public class MySqlJdbcExecutor implements JdbcExecutor {

    private final DbHolder dbHolder;

    @Qualifier(AppConstant.IO_POOL_NAME)
    private final ThreadPoolExecutor ioPool;

    @Qualifier(AppConstant.CALCULATE_POOL_NAME)
    private final ThreadPoolExecutor calculatePool;

    @Override
    public <T> void batchInsert(List<T> dataList) {

    }

    @Override
    public <T> List<T> batchQueryAll(Class<T> clazz, String where) {
        Integer totalCount = count(clazz, where);
        int taskSize = obtainTaskSize(totalCount);
        List<Long> startIdList = startIdEveryWorker(clazz, where, taskSize);
        return null;
    }

    private int obtainTaskSize(Integer totalCount) {
        int workerSize = ioPool.getCorePoolSize();
        int remain = totalCount % workerSize;
        return totalCount / workerSize + remain;
    }

    private <T> List<Long> startIdEveryWorker(Class<T> clazz, String where, int taskSize) {
        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(clazz);
        String tableName = dbHolder.tableName(clazz);
        int workerSize = ioPool.getCorePoolSize();
        StringBuilder startIdEveryWorkerSqlBuilder = new StringBuilder("SET @last_max_id=0;")
                .append(" SELECT 0 ");
        if (where == null || where.isEmpty()) {
            where = " WHERE ";
        } else {
            where = where + " AND ";
        }
        String oneIdSql = String.format(" SELECT max_id FROM (SELECT @last_max_id:=max(id) as max_id FROM (SELECT id FROM %s %s id > (SELECT @last_max_id) limit %s) tmp) t1 ", tableName, where, taskSize);
        for (int i = 0; i < workerSize; i++) {
            startIdEveryWorkerSqlBuilder.append(" UNION ALL ")
                    .append(oneIdSql);
        }

        List<Long> startIdList = jdbcTemplate.queryForList(startIdEveryWorkerSqlBuilder.toString(), Long.class);
        return startIdList;
    }

    private <T> Integer count(Class<T> clazz, String where) {
        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(clazz);
        String tableName = dbHolder.tableName(clazz);
        StringBuilder countSqlBuilder = new StringBuilder("SELECT COUNT(id) FROM ").append(tableName);

        if (where != null && !where.isEmpty()) {
            countSqlBuilder.append(where);
        }
        return jdbcTemplate.queryForObject(countSqlBuilder.toString(), Integer.class);
    }
}
