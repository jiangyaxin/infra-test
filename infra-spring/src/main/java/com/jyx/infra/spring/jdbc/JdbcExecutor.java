package com.jyx.infra.spring.jdbc;

import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import com.jyx.infra.spring.jdbc.writer.BatchInsertResult;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author jiangyaxin
 * @since 2023/11/20 13:38
 */
public interface JdbcExecutor {

    void truncate(JdbcTemplate jdbcTemplate, String tableName);

    <T> int batchInsert(JdbcTemplate jdbcTemplate,
                        List<T> dataList, Field[] fields,
                        Insert insert,
                        int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    <T> BatchInsertResult batchInsertAsync(JdbcTemplate jdbcTemplate,
                                           List<T> dataList, Field[] fields,
                                           Insert insert,
                                           int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);


    <OUT> List<OUT> batchQueryAll(JdbcTemplate jdbcTemplate,
                                  Query query, Constructor<OUT> constructor,
                                  int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    <T, OUT> List<OUT> batchQueryAllAndProcess(JdbcTemplate jdbcTemplate,
                                               Query query, Constructor<T> constructor,
                                               ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                               int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAsync(JdbcTemplate jdbcTemplate,
                                                                   Query query, Constructor<T> constructor,
                                                                   int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAndProcessAsync(JdbcTemplate jdbcTemplate,
                                                                             Query query, Constructor<T> constructor,
                                                                             ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                                                             int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    <OUT> List<CompletableFuture<List<OUT>>> batchQueryAllNotInstanceAsync(JdbcTemplate jdbcTemplate,
                                                                           Query query, ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor,
                                                                           int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);
}
