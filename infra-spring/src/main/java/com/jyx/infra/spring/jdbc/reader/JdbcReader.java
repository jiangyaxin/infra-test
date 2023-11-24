package com.jyx.infra.spring.jdbc.reader;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author jiangyaxin
 * @since 2023/11/24 14:11
 */
public interface JdbcReader<T> {

    List<CompletableFuture<T>> batchQueryAsync(JdbcTemplate jdbcTemplate,
                                               String tableName, String select, String where, Object[] args,
                                               int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);
}
