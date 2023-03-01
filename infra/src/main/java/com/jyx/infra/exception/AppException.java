package com.jyx.infra.exception;

/**
 * 不含错误码的异常
 *
 * @author JYX
 * @since 2021/11/5 14:46
 */
public class AppException extends RuntimeException {

    public static AppException of(String message) {
        return new AppException(message);
    }

    public AppException() {
        super();
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(Throwable cause) {
        super(cause);
    }


}
