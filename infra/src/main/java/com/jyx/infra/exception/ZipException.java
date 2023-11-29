package com.jyx.infra.exception;

/**
 * 不含错误码的异常
 *
 * @author jiangyaxin
 * @since 2021/11/5 14:46
 */
public class ZipException extends RuntimeException {

    public static ZipException of(String message) {
        return new ZipException(message);
    }

    public ZipException() {
        super();
    }

    public ZipException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZipException(String message) {
        super(message);
    }

    public ZipException(Throwable cause) {
        super(cause);
    }


}
