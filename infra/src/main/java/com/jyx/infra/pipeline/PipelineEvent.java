package com.jyx.infra.pipeline;

import java.util.concurrent.CompletableFuture;

/**
 * @author jiangyaxin
 * @since 2023/11/1 15:26
 */
public class PipelineEvent<T> {

    private Long sequence;

    private StageDefinition stageDefinition;

    private T data;

    private CompletableFuture<Void> future;

    public void load(Long sequence, T data, CompletableFuture<Void> future) {
        this.sequence = sequence;
        this.data = data;
        this.future = future;
    }

    public void stage(StageDefinition stageDefinition) {
        this.stageDefinition = stageDefinition;
    }

    public void clear() {
        this.sequence = null;
        this.stageDefinition = null;
        this.data = null;
        this.future = null;
    }


    public Long sequence() {
        return sequence;
    }


    public StageDefinition stage() {
        return stageDefinition;
    }

    public T data() {
        return data;
    }

    public CompletableFuture<Void> future() {
        return future;
    }
}
