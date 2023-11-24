package com.jyx.infra.exception;

/**
 * @author jiangyaxin
 * @since 2023/11/6 10:24
 */
public class TransferException extends RuntimeException {

    public static TransferException of(String message) {
        return new TransferException(message);
    }

    public static TransferException of(String message, Throwable cause) {
        return new TransferException(message, cause);
    }

    public static TransferException of(Throwable cause) {
        return new TransferException(cause);
    }

    public TransferException() {
    }

    public TransferException(String message) {
        super(message);
    }

    public TransferException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransferException(Throwable cause) {
        super(cause);
    }

    public TransferException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
