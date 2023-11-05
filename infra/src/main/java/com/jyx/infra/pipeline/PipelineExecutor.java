package com.jyx.infra.pipeline;

import java.util.concurrent.CompletableFuture;

/**
 * @author Archforce
 * @since 2023/11/1 10:10
 */
public interface PipelineExecutor<T> {

    String name();

    PipelineState state();

    void start();


    void stop();

    void addStage(Iterable<StageDefinition<T>> stageIterable);

    CompletableFuture<Void> submit(T data);

    CompletableFuture<Void> trySubmit(T data);

}
