package com.jyx.infra.serializable;

import com.jyx.infra.thread.FutureResult;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @author Archforce
 * @since 2023/9/28 14:19
 */
public interface Serializer {

    <T> SerializeResult serialize(Collection<T> dataCollection, String filePath);

    <T> List<T> deserialize(Class<T> clazz, String filePath);

    <T> List<T> deserialize(Class<T> clazz, Collection<String> filePathCollection);

    <T> List<T> deserialize(Class<T> clazz, String... filePaths);

    <T> CompletableFuture<FutureResult<List<T>>> deserializeSync(ExecutorService executorService, Class<T> clazz, String filePath);

    <T> List<CompletableFuture<FutureResult<List<T>>>> deserializeSync(ExecutorService executorService, Class<T> clazz, Collection<String> filePathCollection);

    <T> List<CompletableFuture<FutureResult<List<T>>>> deserializeSync(ExecutorService executorService, Class<T> clazz, String... filePaths);

}
