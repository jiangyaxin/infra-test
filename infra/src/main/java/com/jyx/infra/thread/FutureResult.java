package com.jyx.infra.thread;

import com.jyx.infra.log.Logs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Archforce
 * @since 2023/10/5 10:53
 */
@Slf4j
@Getter
@AllArgsConstructor(staticName = "of")
public class FutureResult<T> {

    private T data;

    private Throwable throwable;

    public boolean exceptionally() {
        return throwable != null;
    }

    public static <T> List<T> mergeCompletableFuture(List<CompletableFuture<T>> futureList) {
        List<CompletableFuture<FutureResult<T>>> resultFutureList = new ArrayList<>(futureList.size());
        for (CompletableFuture<T> future : futureList) {
            CompletableFuture<FutureResult<T>> resultFuture = future.handle((result, ex) -> {
                if (ex != null) {
                    Logs.error(log, "CompletableFuture failed.", ex);
                }
                return FutureResult.of(result, ex);
            });
            resultFutureList.add(resultFuture);
        }

        List<T> result = new ArrayList<>(futureList.size());
        for (CompletableFuture<FutureResult<T>> resultFuture : resultFutureList) {
            try {
                FutureResult<T> futureResult = resultFuture.get();
                if (futureResult.exceptionally()) {
                    throw futureResult.throwable;
                }
                result.add(futureResult.data);
            } catch (Throwable e) {
                throw FutureException.of("Merge CompletableFuture failed", e);
            }
        }
        return result;
    }
}
