package com.jyx.infra.exception;

/**
 * 不含错误码的异常
 *
 * @author jiangyaxin
 * @since 2021/11/5 14:46
 */
public class JsonException extends RuntimeException {

    public static JsonException of(String message) {
        return new JsonException(message);
    }

    public JsonException() {
        super();
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(Throwable cause) {
        super(cause);
    }


}
