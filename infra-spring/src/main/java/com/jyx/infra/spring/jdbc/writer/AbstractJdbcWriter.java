package com.jyx.infra.spring.jdbc.writer;

import com.google.common.collect.Lists;
import com.jyx.infra.asserts.Asserts;
import com.jyx.infra.log.Logs;
import com.jyx.infra.spring.jdbc.Insert;
import com.jyx.infra.thread.FutureResult;
import com.jyx.infra.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Archforce
 * @since 2023/11/25 11:14
 */
@Slf4j
public abstract class AbstractJdbcWriter implements JdbcWriter {

    protected final ThreadPoolExecutor ioPool;

    public AbstractJdbcWriter(ThreadPoolExecutor ioPool) {
        this.ioPool = ioPool;
    }

    protected abstract ExecutorService getDataExtractorPool();

    protected abstract String buildInsertSql(String tableName, String[] columns);

    @Override
    public <T> int batchInsert(JdbcTemplate jdbcTemplate,
                               List<T> dataList, Field[] fields,
                               Insert insert,
                               int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        if (CollectionUtils.isEmpty(dataList)) {
            return 0;
        }
        BatchInsertResult batchInsertResult = batchInsertAsync(jdbcTemplate,
                dataList, fields,
                insert,
                taskSizeOfEachWorker, onceBatchSizeOfEachWorker);

        return batchInsertResult.getResult();
    }

    @Override
    public <T> BatchInsertResult batchInsertAsync(JdbcTemplate jdbcTemplate,
                                                  List<T> dataList, Field[] fields,
                                                  Insert insert,
                                                  int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        ExecutorService extractorPool = getDataExtractorPool();
        AsyncReflectInsertDataExtractor dataExtractor = new AsyncReflectInsertDataExtractor(fields, extractorPool, taskSizeOfEachWorker);
        List<CompletableFuture<List<Object[]>>> extractFutureList = dataExtractor.extract(dataList);
        List<CompletableFuture<BatchInsertResult>> resultFutureList = new ArrayList<>();
        for (CompletableFuture<List<Object[]>> future : extractFutureList) {
            CompletableFuture<BatchInsertResult> resultFuture = future.thenApplyAsync(objectsList ->
                    batchInsertAsync(jdbcTemplate, objectsList,
                            insert,
                            taskSizeOfEachWorker, onceBatchSizeOfEachWorker), ForkJoinPool.commonPool());
            resultFutureList.add(resultFuture);
        }
        List<BatchInsertResult> resultList = FutureResult.mergeCompletableFuture(resultFutureList);
        return BatchInsertResult.merge(resultList);
    }

    @Override
    public int batchInsert(JdbcTemplate jdbcTemplate,
                           List<Object[]> dataList,
                           Insert insert,
                           int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {
        if (CollectionUtils.isEmpty(dataList)) {
            return 0;
        }
        BatchInsertResult batchInsertResult = batchInsertAsync(jdbcTemplate, dataList, insert, taskSizeOfEachWorker, onceBatchSizeOfEachWorker);

        return batchInsertResult.getResult();
    }

    @Override
    public BatchInsertResult batchInsertAsync(JdbcTemplate jdbcTemplate,
                                              List<Object[]> dataList,
                                              Insert insert,
                                              int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker) {

        if (CollectionUtils.isEmpty(dataList)) {
            return BatchInsertResult.EMPTY_RESULT;
        }
        String tableName = insert.getTableName();
        String[] columns = insert.getColumns();
        int[] columnTypes = insert.getColumnTypes();

        Asserts.isTrue(columns != null && columns.length != 0, () -> "Batch insert: columns must be required.");
        Asserts.isTrue(dataList.get(0).length == columns.length, () -> "Batch insert: columns and one record data length must be same quantity");

        int totalCount = dataList.size();
        int workerSize = PageUtil.calculateNumberOfPage(totalCount, taskSizeOfEachWorker);
        List<List<Object[]>> partitionList = Lists.partition(dataList, taskSizeOfEachWorker);

        String insertSql = buildInsertSql(tableName, columns);

        List<CompletableFuture<Integer>> futureList = new ArrayList<>(workerSize);
        List<InterruptBatchInsertArgsSetter> pssList = new ArrayList<>(workerSize);
        for (List<Object[]> partition : partitionList) {
            InterruptBatchInsertArgsSetter pss = new InterruptBatchInsertArgsSetter(onceBatchSizeOfEachWorker, columns, columnTypes, true) {
                @Override
                protected boolean hasNextAvailableData(int i) {
                    return i < partition.size();
                }

                @Override
                protected Object[] getCurrentData(int i) {
                    return partition.get(i);
                }
            };

            InterruptBatchInsertPreparedStatementCallBack preparedStatementCallBack = new InterruptBatchInsertPreparedStatementCallBack(pss);

            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return jdbcTemplate.execute(insertSql, preparedStatementCallBack);
                } catch (Exception e) {
                    Logs.error(log, "Batch insert error", e);
                    throw e;
                }
            }, ioPool);
            futureList.add(future);
            pssList.add(pss);
        }

        return new BatchInsertResult(futureList, pssList);
    }
}
