package com.jyx.infra.mybatis.plus.jdbc;

import com.jyx.infra.mybatis.plus.jdbc.common.ResultSetExtractPostProcessor;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Archforce
 * @since 2023/11/20 13:38
 */
public interface JdbcExecutor {

    <T> void batchInsert(List<T> dataList);

    <T, OUT> List<OUT> batchQueryAll(Class<T> clazz, String where,
                                     int taskSize, int batchSize);

    <T, OUT> List<OUT> batchQueryAll(Class<T> clazz, Constructor<T> constructor,
                                     String select, String where,
                                     int taskSize, int batchSize);

    <T, OUT> List<OUT> batchQueryAllAndProcess(Class<T> clazz, String where,
                                               ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                               int taskSize, int batchSize);

    <T, OUT> List<OUT> batchQueryAllAndProcess(Class<T> clazz, Constructor<T> constructor,
                                               String select, String where,
                                               ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                               int taskSize, int batchSize);

    <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAsync(Class<T> clazz, String where,
                                                                   int taskSize, int batchSize);

    <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAsync(Class<T> clazz, Constructor<T> constructor,
                                                                   String select, String where,
                                                                   int taskSize, int batchSize);

    <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAndProcessAsync(Class<T> clazz, String where,
                                                                             ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                                                             int taskSize, int batchSize);

    <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAndProcessAsync(Class<T> clazz, Constructor<T> constructor,
                                                                             String select, String where,
                                                                             ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                                                             int taskSize, int batchSize);

    <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllNotInstanceAsync(Class<T> clazz,
                                                                              String select, String where,
                                                                              ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor,
                                                                              int taskSize, int batchSize);
}
