package com.jyx.infra.pipeline;

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

    boolean submit(T data);

    boolean trySubmit(T data);

}
