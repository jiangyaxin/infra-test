package com.jyx.infra.exception;

/**
 * 不含错误码的异常
 *
 * @author jiangyaxin
 * @since 2021/11/5 14:46
 */
public class ExcelException extends RuntimeException {

    public static ExcelException of(String message) {
        return new ExcelException(message);
    }

    public ExcelException() {
        super();
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }


}
