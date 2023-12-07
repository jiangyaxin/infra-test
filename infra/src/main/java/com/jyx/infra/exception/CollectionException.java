package com.jyx.infra.exception;

/**
 * 不含错误码的异常
 *
 * @author jiangyaxin
 * @since 2021/11/5 14:46
 */
public class CollectionException extends RuntimeException {

    public static CollectionException of(String message) {
        return new CollectionException(message);
    }

    public CollectionException() {
        super();
    }

    public CollectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectionException(String message) {
        super(message);
    }

    public CollectionException(Throwable cause) {
        super(cause);
    }


}
