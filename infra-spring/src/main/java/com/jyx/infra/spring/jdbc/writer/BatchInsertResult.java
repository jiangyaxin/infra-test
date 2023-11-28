package com.jyx.infra.spring.jdbc.writer;

import com.jyx.infra.log.Logs;
import com.jyx.infra.thread.FutureResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Archforce
 * @since 2023/11/25 17:11
 */
@Slf4j
public class BatchInsertResult {

    public static final BatchInsertResult EMPTY_RESULT = new BatchInsertResult(new ArrayList<>(0), new ArrayList<>(0));

    private final List<CompletableFuture<Integer>> futureList;

    private final List<InterruptBatchInsertArgsSetter> pssList;

    public BatchInsertResult(List<CompletableFuture<Integer>> futureList,
                             List<InterruptBatchInsertArgsSetter> pssList) {
        this.futureList = futureList;
        this.pssList = pssList;
    }

    /**
     * 数据插入到一半不会回退，单纯只是不继续插入未插入的数据
     */
    public void interrupt() {
        if (!CollectionUtils.isEmpty(pssList)) {
            pssList.forEach(pss -> pss.interrupt());
        }
        if (!CollectionUtils.isEmpty(futureList)) {
            futureList.forEach(future -> future.cancel(true));
            try {
                CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
            } catch (Exception ex) {
                Throwable cause = ex.getCause();
                if (cause instanceof CancellationException) {
                    Logs.warn(log, "Task cancellation.", cause);
                } else {
                    throw ex;
                }
            }
        }
    }

    public int getResult() {
        if (CollectionUtils.isEmpty(futureList)) {
            return 0;
        }
        int count = 0;
        List<Integer> rowsAffectedList = FutureResult.mergeCompletableFuture(futureList);
        for (Integer rowsAffected : rowsAffectedList) {
            count += rowsAffected;
        }
        return count;
    }

    public static BatchInsertResult merge(List<BatchInsertResult> resultList) {
        int futureSize = 0;
        int pssSize = 0;
        for (BatchInsertResult result : resultList) {
            futureSize += result.futureList.size();
            pssSize += result.pssList.size();
        }

        List<CompletableFuture<Integer>> mergeFutureList = new ArrayList<>(futureSize);
        List<InterruptBatchInsertArgsSetter> mergePssList = new ArrayList<>(pssSize);

        for (BatchInsertResult result : resultList) {
            mergeFutureList.addAll(result.futureList);
            mergePssList.addAll(result.pssList);
        }

        return new BatchInsertResult(mergeFutureList, mergePssList);
    }
}
