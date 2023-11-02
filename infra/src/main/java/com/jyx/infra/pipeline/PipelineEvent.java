package com.jyx.infra.pipeline;

/**
 * @author Archforce
 * @since 2023/11/1 15:26
 */
public class PipelineEvent<T> {

    private Long sequence;

    private StageDefinition stageDefinition;

    private T data;

    public void load(Long sequence, T data) {
        this.sequence = sequence;
        this.data = data;
    }

    public void stage(StageDefinition stageDefinition) {
        this.stageDefinition = stageDefinition;
    }

    public void clear() {
        this.sequence = null;
        this.stageDefinition = null;
        this.data = null;
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
}
