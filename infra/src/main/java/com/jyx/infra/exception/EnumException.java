package com.jyx.infra.exception;

/**
 * 不含错误码的异常
 *
 * @author jiangyaxin
 * @since 2021/11/5 14:46
 */
public class EnumException extends RuntimeException {

    public static EnumException of(String message) {
        return new EnumException(message);
    }

    public EnumException() {
        super();
    }

    public EnumException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnumException(String message) {
        super(message);
    }

    public EnumException(Throwable cause) {
        super(cause);
    }


}
