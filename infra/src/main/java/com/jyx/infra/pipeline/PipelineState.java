package com.jyx.infra.pipeline;

import lombok.Getter;

/**
 * @author jiangyaxin
 * @since 2023/11/1 10:32
 */
@Getter
public enum PipelineState {

    READY,

    RUNNING,

    STOP;

    public PipelineState nextState() {
        switch (this) {
            case READY:
                return RUNNING;
            case RUNNING:
                return STOP;
            case STOP:
                throw new PipelineException("Pipeline is stop.");
            default:
                throw new PipelineException(String.format("Error pipeline state: %s", this.name()));
        }
    }
}
