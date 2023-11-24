package com.jyx.infra.spring.jdbc;

import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author jiangyaxin
 * @since 2023/11/20 13:38
 */
public interface JdbcExecutor {

    <T> void batchInsert(List<T> dataList);


    <OUT> List<OUT> batchQueryAll(JdbcTemplate jdbcTemplate,
                                     String tableName, String select, String where, Object[] args, Constructor<OUT> constructor,
                                     int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    <T, OUT> List<OUT> batchQueryAllAndProcess(JdbcTemplate jdbcTemplate,
                                               String tableName, String select, String where, Object[] args, Constructor<T> constructor,
                                               ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                               int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAsync(JdbcTemplate jdbcTemplate,
                                                                   String tableName, String select, String where, Object[] args, Constructor<T> constructor,
                                                                   int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAndProcessAsync(JdbcTemplate jdbcTemplate,
                                                                             String tableName, String select, String where, Object[] args, Constructor<T> constructor,
                                                                             ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                                                             int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    <OUT> List<CompletableFuture<List<OUT>>> batchQueryAllNotInstanceAsync(JdbcTemplate jdbcTemplate,
                                                                         String tableName, String select, String where, Object[] args,
                                                                         ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor,
                                                                           int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);
}
