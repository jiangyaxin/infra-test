package com.jyx.infra.exception;

/**
 * @author Archforce
 * @since 2023/11/6 10:24
 */
public class ReflectionException extends RuntimeException {

    public static ReflectionException of(String message) {
        return new ReflectionException(message);
    }

    public static ReflectionException of(String message, Throwable cause) {
        return new ReflectionException(message, cause);
    }

    public static ReflectionException of(Throwable cause) {
        return new ReflectionException(cause);
    }

    public ReflectionException() {
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }

    public ReflectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
