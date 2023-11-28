package com.jyx.infra.spring.jdbc;

import com.jyx.infra.constant.RuntimeConstant;
import com.jyx.infra.spring.jdbc.reader.InstancePostProcessorResultSet;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import com.jyx.infra.spring.jdbc.writer.BatchInsertResult;
import com.jyx.infra.spring.jdbc.writer.JdbcWriter;
import com.jyx.infra.thread.FutureResult;
import com.jyx.infra.thread.ThreadPoolExecutors;
import com.jyx.infra.util.ListUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static com.jyx.infra.spring.jdbc.AbstractJdbcExecutor.Constants.*;

/**
 * @author jiangyaxin
 * @since 2023/11/24 9:29
 */
public abstract class AbstractJdbcExecutor implements JdbcExecutor {

    interface Constants {
        int processors = RuntimeConstant.PROCESSORS;

        int factor = 5;

        String POOL_NAME = "Jdbc-IO-Pool";
        int CORE_SIZE = processors * 2;
        int MAX_SIZE = processors * factor;
        int QUEUE_SIZE = 1024 * factor;
    }

    protected final ThreadPoolExecutor ioPool;

    protected JdbcWriter jdbcWriter;

    public AbstractJdbcExecutor() {
        if (ThreadPoolExecutors.threadPoolExist(POOL_NAME)) {
            this.ioPool = ThreadPoolExecutors.getThreadPool(POOL_NAME);
        } else {
            this.ioPool = ThreadPoolExecutors.newThreadPool(CORE_SIZE, MAX_SIZE, QUEUE_SIZE, POOL_NAME);
        }
        this.jdbcWriter = buildJdbcWriter();
    }

    protected abstract JdbcWriter buildJdbcWriter();

    @Override
    public void truncate(JdbcTemplate jdbcTemplate, String tableName) {
        String truncateSql = KeyWorkConstant.TRUNCATE + KeyWorkConstant.EMPTY + tableName;
        jdbcTemplate.execute(truncateSql);
    }

    @Override
    public <T> int batchInsert(JdbcTemplate jdbcTemplate,
                               List<T> dataList, Field[] fields,
                               Insert insert,
                               int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        int rowsAffected = jdbcWriter.batchInsert(jdbcTemplate,
                dataList, fields,
                insert,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker);
        return rowsAffected;
    }

    @Override
    public <T> BatchInsertResult batchInsertAsync(JdbcTemplate jdbcTemplate,
                                                  List<T> dataList, Field[] fields,
                                                  Insert insert,
                                                  int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        BatchInsertResult batchInsertResult = jdbcWriter.batchInsertAsync(jdbcTemplate,
                dataList, fields,
                insert,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker);
        return batchInsertResult;
    }

    @Override
    public <OUT> List<OUT> batchQueryAll(JdbcTemplate jdbcTemplate,
                                         Query query, Constructor<OUT> constructor,
                                         int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        List<CompletableFuture<List<OUT>>> futureList = batchQueryAllAsync(jdbcTemplate,
                query, constructor,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker
        );
        return mergeFuture(futureList);
    }

    @Override
    public <T, OUT> List<OUT> batchQueryAllAndProcess(JdbcTemplate jdbcTemplate,
                                                      Query query, Constructor<T> constructor,
                                                      ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                                      int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        List<CompletableFuture<List<OUT>>> futureList = batchQueryAllAndProcessAsync(jdbcTemplate,
                query, constructor,
                instancePostProcessor,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker
        );
        return mergeFuture(futureList);
    }

    @Override
    public <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAsync(JdbcTemplate jdbcTemplate,
                                                                          Query query, Constructor<T> constructor,
                                                                          int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        InstancePostProcessorResultSet<T, OUT> extractPostProcessor = new InstancePostProcessorResultSet<>(constructor);
        return batchQueryAllNotInstanceAsync(jdbcTemplate, query, extractPostProcessor,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker);
    }

    @Override
    public <T, OUT> List<CompletableFuture<List<OUT>>> batchQueryAllAndProcessAsync(JdbcTemplate jdbcTemplate,
                                                                                    Query query, Constructor<T> constructor,
                                                                                    ResultSetExtractPostProcessor<T, OUT> instancePostProcessor,
                                                                                    int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        InstancePostProcessorResultSet<T, OUT> extractPostProcessor = new InstancePostProcessorResultSet<>(constructor, instancePostProcessor, true);
        return batchQueryAllNotInstanceAsync(jdbcTemplate, query, extractPostProcessor,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker);
    }

    protected <OUT> List<OUT> mergeFuture(List<CompletableFuture<List<OUT>>> futureList) {
        List<List<OUT>> twoDimensionalList = FutureResult.mergeCompletableFuture(futureList);
        List<OUT> result = ListUtil.flatList(twoDimensionalList);
        return result;
    }
}
