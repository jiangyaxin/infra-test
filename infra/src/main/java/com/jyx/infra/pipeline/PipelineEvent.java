package com.jyx.infra.pipeline;

/**
 * @author Archforce
 * @since 2023/11/1 15:26
 */
public class PipelineEvent<T> {

    private Long sequence;

    private Stage stage;

    private T data;

    public void load(Long sequence, T data) {
        this.sequence = sequence;
        this.data = data;
    }

    public void stage(Stage stage) {
        this.stage = stage;
    }

    public void clear() {
        this.sequence = null;
        this.stage = null;
        this.data = null;
    }


    public Long sequence() {
        return sequence;
    }


    public Stage stage() {
        return stage;
    }

    public T data() {
        return data;
    }
}
