package com.jyx.infra.spring.jdbc;

import com.jyx.infra.spring.context.AppConstant;
import com.jyx.infra.spring.jdbc.reader.InstancePostProcessorResultSet;
import com.jyx.infra.spring.jdbc.reader.JdbcReader;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import com.jyx.infra.thread.FutureResult;
import com.jyx.infra.thread.ThreadPoolExecutors;
import com.jyx.infra.util.ListUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static com.jyx.infra.spring.jdbc.MultiThreadJdbcExecutor.Constants.*;

/**
 * @author jiangyaxin
 * @since 2023/11/24 9:29
 */
public abstract class MultiThreadJdbcExecutor implements JdbcExecutor {

    interface Constants {
        int processors = AppConstant.PROCESSORS;

        int factor = 10;

        String POOL_NAME = "Jdbc-IO-Pool";
        int CORE_SIZE = processors * 2;
        int MAX_SIZE = processors * factor;
        int QUEUE_SIZE = 1024 * factor;
    }

    protected final ThreadPoolExecutor ioPool;

    public MultiThreadJdbcExecutor() {
        this.ioPool = ThreadPoolExecutors.newThreadPool(CORE_SIZE, MAX_SIZE, QUEUE_SIZE, POOL_NAME);
    }

    protected abstract <OUT> JdbcReader<List<OUT>> buildJdbcReader(ThreadPoolExecutor ioPool, ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor);

    @Override
    public <T> void batchInsert(List<T> dataList) {

    }

    @Override
    public <T, OUT> List<OUT> batchQueryAll(JdbcTemplate jdbcTemplate,
                                            String tableName, String select, String where, Object[] args, Constructor<T> constructor,
                                            int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        List<CompletableFuture<List<OUT>>> futureList = batchQueryAllAsync(jdbcTemplate,
                tableName, select, where, args, constructor,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker
        );
        return mergeFuture(futureList);
    }

    @Override
    public <T, OUT> List<OUT> batchQueryAllAndProcess(JdbcTemplate jdbcTemplate,
                                                      String tableName, String select, String where, Object[] args, Constructor<T> constructor,
                                                      ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                                      int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        List<CompletableFuture<List<OUT>>> futureList = batchQueryAllAndProcessAsync(jdbcTemplate,
                tableName, select, where, args, constructor,
                instancePostProcessor,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker
        );
        return mergeFuture(futureList);
    }

    @Override
    public <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAsync(JdbcTemplate jdbcTemplate,
                                                                          String tableName, String select, String where, Object[] args, Constructor<T> constructor,
                                                                          int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        InstancePostProcessorResultSet<T, OUT> extractPostProcessor = new InstancePostProcessorResultSet<>(constructor);
        return batchQueryAllNotInstanceAsync(jdbcTemplate,
                tableName, select, where, args,
                extractPostProcessor,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker);
    }

    @Override
    public <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAndProcessAsync(JdbcTemplate jdbcTemplate,
                                                                                    String tableName, String select, String where, Object[] args, Constructor<T> constructor,
                                                                                    ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                                                                    int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        InstancePostProcessorResultSet<T, OUT> extractPostProcessor = new InstancePostProcessorResultSet<>(constructor, instancePostProcessor, true);
        return batchQueryAllNotInstanceAsync(jdbcTemplate,
                tableName, select, where, args,
                extractPostProcessor,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker);
    }

    @Override
    public <OUT> List<CompletableFuture<List<OUT>>> batchQueryAllNotInstanceAsync(JdbcTemplate jdbcTemplate,
                                                                                  String tableName, String select, String where, Object[] args,
                                                                                  ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor,
                                                                                  int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        JdbcReader<List<OUT>> jdbcReader = buildJdbcReader(ioPool, extractPostProcessor);

        List<CompletableFuture<List<OUT>>> futureList = jdbcReader.batchQueryAsync(jdbcTemplate,
                tableName, select, where, args,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker);
        return futureList;
    }

    protected <OUT> List<OUT> mergeFuture(List<CompletableFuture<List<OUT>>> futureList) {
        List<List<OUT>> twoDimensionalList = FutureResult.mergeCompletableFuture(futureList);
        List<OUT> result = ListUtil.flatList(twoDimensionalList);
        return result;
    }
}
