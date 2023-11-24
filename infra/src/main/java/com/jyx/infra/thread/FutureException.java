package com.jyx.infra.thread;

/**
 * @author jiangyaxin
 * @since 2023/11/1 10:44
 */
public class FutureException extends RuntimeException {

    public static FutureException of(String message) {
        return new FutureException(message);
    }

    public static FutureException of(String message, Throwable cause) {
        return new FutureException(message, cause);
    }

    public FutureException() {
    }

    public FutureException(String message) {
        super(message);
    }

    public FutureException(String message, Throwable cause) {
        super(message, cause);
    }

    public FutureException(Throwable cause) {
        super(cause);
    }

    public FutureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
