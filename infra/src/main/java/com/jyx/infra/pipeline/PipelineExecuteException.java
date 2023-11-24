package com.jyx.infra.pipeline;

/**
 * @author jiangyaxin
 * @since 2023/11/1 10:44
 */
public class PipelineExecuteException extends RuntimeException {

    public static PipelineExecuteException of(String message) {
        return new PipelineExecuteException(message);
    }

    public static PipelineExecuteException of(String message, Throwable cause) {
        return new PipelineExecuteException(message, cause);
    }

    public PipelineExecuteException() {
    }

    public PipelineExecuteException(String message) {
        super(message);
    }

    public PipelineExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public PipelineExecuteException(Throwable cause) {
        super(cause);
    }

    public PipelineExecuteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
