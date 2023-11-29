package com.jyx.infra.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.jyx.infra.exception.ExcelException;
import com.jyx.infra.thread.FutureResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * @author Archforce
 * @since 2023/11/29 17:06
 */
public class AsyncExcelReadListener<T, OUT> implements ReadListener<T> {

    interface Constants {
        int DEFAULT_EXCEPTED_TASK_SIZE = 256;
    }

    private final ExecutorService executorService;

    private final int taskSizeOfEachWorker;

    private final Function<List<T>, OUT> dataProcessor;

    private List<T> cachedDataList;

    private List<CompletableFuture<OUT>> futureList;

    private final CompletableFuture overFuture;

    public AsyncExcelReadListener(ExecutorService executorService,
                                  int taskSizeOfEachWorker, int exceptedTaskSize,
                                  Function<List<T>, OUT> dataProcessor) {
        this.executorService = executorService;
        this.taskSizeOfEachWorker = taskSizeOfEachWorker;
        this.dataProcessor = dataProcessor;
        this.cachedDataList = new ArrayList<>(taskSizeOfEachWorker);
        this.futureList = new ArrayList<>(Math.max(exceptedTaskSize, Constants.DEFAULT_EXCEPTED_TASK_SIZE));
        this.overFuture = new CompletableFuture();
    }

    public List<OUT> getResult() {
        try {
            overFuture.get();
        } catch (Exception e) {
            throw new ExcelException(e);
        }

        List<OUT> result = FutureResult.mergeCompletableFuture(futureList);
        futureList = null;
        return result;
    }

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        cachedDataList.add(t);
        if (cachedDataList.size() == taskSizeOfEachWorker) {
            CompletableFuture<OUT> future = submit(cachedDataList);
            futureList.add(future);
            cachedDataList = new ArrayList<>(taskSizeOfEachWorker);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        CompletableFuture<OUT> future = submit(cachedDataList);
        futureList.add(future);
        cachedDataList = null;
        overFuture.complete(null);
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        overFuture.completeExceptionally(exception);
    }

    private CompletableFuture<OUT> submit(List<T> dataList) {
        List<T> localDataList = dataList;
        CompletableFuture<OUT> future = CompletableFuture.supplyAsync(() -> {
            OUT out = dataProcessor.apply(localDataList);
            localDataList.clear();
            return out;
        }, executorService);
        return future;
    }
}
