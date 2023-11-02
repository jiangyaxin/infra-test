package com.jyx.infra.pipeline;

import java.util.concurrent.TimeUnit;

/**
 * @author Archforce
 * @since 2023/11/1 10:10
 */
public interface PipelineExecutor<T> {

    String name();

    PipelineState state();

    void start();

    void stop(long timeout, TimeUnit timeUnit);

    void addStage(Iterable<StageDefinition<T>> stageIterable);

    boolean submit(T data);

    boolean trySubmit(T data);

}
