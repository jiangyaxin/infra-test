package com.jyx.infra.exception;

/**
 * 带 code 的业务异常
 *
 * @author JYX
 * @since 2021/11/5 14:51
 */
public class BusinessException extends AppException {

    private final MessageCode code;

    public static BusinessException of(MessageCode messageCode) {
        return new BusinessException(messageCode);
    }

    public static BusinessException of(MessageCode messageCode, String message) {
        return new BusinessException(messageCode, message);
    }

    public static BusinessException of(MessageCode messageCode, Throwable cause) {
        return new BusinessException(messageCode, cause);
    }

    public static BusinessException of(MessageCode messageCode, String message, Throwable cause) {
        return new BusinessException(messageCode, message, cause);
    }

    public BusinessException(MessageCode messageCode) {
        super();
        this.code = messageCode;
    }

    public BusinessException(MessageCode messageCode, String message) {
        super(message);
        this.code = messageCode;
    }

    public BusinessException(MessageCode messageCode, Throwable cause) {
        super(cause);
        this.code = messageCode;
    }

    public BusinessException(MessageCode messageCode, String message, Throwable cause) {
        super(message, cause);
        this.code = messageCode;
    }

    public MessageCode getCode() {
        return code;
    }
}
