package com.jyx.infra.spring.jdbc;

import com.jyx.infra.constant.RuntimeConstant;
import com.jyx.infra.spring.jdbc.reader.JdbcReader;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import com.jyx.infra.thread.ThreadPoolExecutors;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static com.jyx.infra.spring.jdbc.ReactJdbcExecutor.Constants.*;

/**
 * @author jiangyaxin
 * @since 2023/11/24 12:27
 */
public abstract class ReactJdbcExecutor extends AbstractJdbcExecutor {

    interface Constants {
        int processors = RuntimeConstant.PROCESSORS;

        int factor = 2;

        String POOL_NAME = "Jdbc-Task-Pool";
        int CORE_SIZE = processors + 1;
        int MAX_SIZE = processors * factor;
        int QUEUE_SIZE = 256 * factor;
    }

    protected final ThreadPoolExecutor taskPool;

    public ReactJdbcExecutor() {
        super();
        if (ThreadPoolExecutors.threadPoolExist(POOL_NAME)) {
            this.taskPool = ThreadPoolExecutors.getThreadPool(POOL_NAME);
        } else {
            this.taskPool = ThreadPoolExecutors.newThreadPool(CORE_SIZE, MAX_SIZE, QUEUE_SIZE, POOL_NAME);
        }
    }

    protected abstract <OUT> JdbcReader<List<CompletableFuture<List<OUT>>>> buildJdbcReader(ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor);


    @Override
    public <OUT> List<CompletableFuture<List<OUT>>> batchQueryAllNotInstanceAsync(JdbcTemplate jdbcTemplate,
                                                                                  String tableName, String select, String where, Object[] args,
                                                                                  ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor,
                                                                                  int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        JdbcReader<List<CompletableFuture<List<OUT>>>> jdbcReader = buildJdbcReader(extractPostProcessor);

        List<CompletableFuture<List<CompletableFuture<List<OUT>>>>> futureList = jdbcReader.batchQueryAsync(jdbcTemplate,
                tableName, select, where, args,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker);

        List<CompletableFuture<List<OUT>>> result = mergeFuture(futureList);
        return result;
    }
}
