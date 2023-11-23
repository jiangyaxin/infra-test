package com.jyx.infra.mybatis.plus.jdbc.common;

/**
 * @author Archforce
 * @since 2023/11/22 17:10
 */
public class IncorrectInstanceDataAccessException extends RuntimeException {

    public IncorrectInstanceDataAccessException() {
    }

    public IncorrectInstanceDataAccessException(String message) {
        super(message);
    }

    public IncorrectInstanceDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectInstanceDataAccessException(Throwable cause) {
        super(cause);
    }

    public IncorrectInstanceDataAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
