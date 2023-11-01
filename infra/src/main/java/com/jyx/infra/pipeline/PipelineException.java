package com.jyx.infra.pipeline;

/**
 * @author Archforce
 * @since 2023/11/1 10:44
 */
public class PipelineException extends RuntimeException {

    public static PipelineException of(String message) {
        return new PipelineException(message);
    }

    public static PipelineException of(String message, Throwable cause) {
        return new PipelineException(message, cause);
    }

    public PipelineException() {
    }

    public PipelineException(String message) {
        super(message);
    }

    public PipelineException(String message, Throwable cause) {
        super(message, cause);
    }

    public PipelineException(Throwable cause) {
        super(cause);
    }

    public PipelineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
