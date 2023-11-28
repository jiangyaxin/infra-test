package com.jyx.infra.spring.jdbc.writer;

/**
 * @author jiangyaxin
 * @since 2023/11/22 17:10
 */
public class IncorrectInsertDataException extends RuntimeException {

    public IncorrectInsertDataException() {
    }

    public IncorrectInsertDataException(String message) {
        super(message);
    }

    public IncorrectInsertDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectInsertDataException(Throwable cause) {
        super(cause);
    }

    public IncorrectInsertDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
