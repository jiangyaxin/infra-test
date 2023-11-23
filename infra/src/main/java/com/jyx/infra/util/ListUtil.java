package com.jyx.infra.util;

import com.google.common.collect.Lists;
import com.jyx.infra.exception.TransferException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Archforce
 * @since 2023/11/6 11:32
 */
public class ListUtil {

    public static <T> List<T> flatList(List<List<T>> twoDimensionalList) {
        int sum = twoDimensionalList.stream()
                .mapToInt(List::size)
                .sum();
        List<T> result = new ArrayList<>(sum);
        twoDimensionalList.forEach(result::addAll);
        return result;
    }

    public static <IN, OUT> List<OUT> parallelTransfer(List<IN> dataList, Function<IN, OUT> transferFunction,
                                                       int batchSize, ExecutorService executorService) {
        Function<List<IN>, List<OUT>> partitionTransferFunction = partition -> {
            List<OUT> partitionResult = new ArrayList<>(partition.size());
            for (IN record : partition) {
                OUT recordResult = transferFunction.apply(record);
                if (recordResult == null) {
                    continue;
                }
                partitionResult.add(recordResult);
            }
            return partitionResult;
        };

        List<OUT> result = parallelTransfer(dataList, partitionTransferFunction, ListUtil::flatList, batchSize, executorService);
        return result;
    }

    public static <IN, MID, OUT> List<OUT> parallelTransfer(List<IN> dataList, Function<List<IN>, MID> partitionTransferFunction,
                                                            Function<List<MID>, List<OUT>> resultMergeFunction,
                                                            int batchSize, ExecutorService executorService) {
        List<List<IN>> partitionList = Lists.partition(dataList, batchSize);

        CopyOnWriteArrayList<Throwable> errorList = new CopyOnWriteArrayList<>();
        List<MID> partitionResultList = new ArrayList<>(partitionList.size());

        List<CompletableFuture<MID>> futureList = partitionList.stream()
                .map(partition -> {
                    CompletableFuture<MID> future = CompletableFuture
                            .supplyAsync(() -> partitionTransferFunction.apply(partition), executorService)
                            .whenComplete((partitionResult, ex) -> {
                                if (ex != null) {
                                    errorList.add(ex);
                                    return;
                                }
                                partitionResultList.add(partitionResult);
                            });
                    return future;
                })
                .collect(Collectors.toCollection(() -> new ArrayList<>(partitionList.size())));

        try {
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!errorList.isEmpty()) {
            throw new TransferException(errorList.get(0));
        }

        List<OUT> result = resultMergeFunction.apply(partitionResultList);
        return result;
    }
}
