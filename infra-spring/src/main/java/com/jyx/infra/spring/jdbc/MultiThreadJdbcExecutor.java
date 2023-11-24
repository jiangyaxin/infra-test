package com.jyx.infra.spring.jdbc;

import com.jyx.infra.spring.jdbc.reader.JdbcReader;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author jiangyaxin
 * @since 2023/11/24 9:29
 */
public abstract class MultiThreadJdbcExecutor extends AbstractJdbcExecutor {

    protected abstract <OUT> JdbcReader<List<OUT>> buildJdbcReader(ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor);


    @Override
    public <OUT> List<CompletableFuture<List<OUT>>> batchQueryAllNotInstanceAsync(JdbcTemplate jdbcTemplate,
                                                                                  String tableName, String select, String where, Object[] args,
                                                                                  ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor,
                                                                                  int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        JdbcReader<List<OUT>> jdbcReader = buildJdbcReader(extractPostProcessor);

        List<CompletableFuture<List<OUT>>> futureList = jdbcReader.batchQueryAsync(jdbcTemplate,
                tableName, select, where, args,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker);
        return futureList;
    }
}
