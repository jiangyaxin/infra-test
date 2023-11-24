package com.jyx.infra.serializable;

/**
 * @author jiangyaxin
 * @since 2023/10/4 15:27
 */
public class SerializableException extends RuntimeException{

    public SerializableException() {
    }

    public SerializableException(String message) {
        super(message);
    }

    public SerializableException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializableException(Throwable cause) {
        super(cause);
    }

    public SerializableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
