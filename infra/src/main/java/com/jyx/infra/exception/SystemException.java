package com.jyx.infra.exception;

/**
 * 带 code 的系统异常
 *
 * @author JYX
 * @since 2021/11/5 15:01
 */
public class SystemException extends AppException {

    private final MessageCode code;

    public static SystemException of(MessageCode messageCode) {
        return new SystemException(messageCode);
    }

    public static SystemException of(MessageCode messageCode, String message) {
        return new SystemException(messageCode, message);
    }

    public static SystemException of(MessageCode messageCode, Throwable cause) {
        return new SystemException(messageCode, cause);
    }

    public static SystemException of(MessageCode messageCode, String message, Throwable cause) {
        return new SystemException(messageCode, message, cause);
    }

    public SystemException(MessageCode messageCode) {
        super();
        this.code = messageCode;
    }

    public SystemException(MessageCode messageCode, String message) {
        super(message);
        this.code = messageCode;
    }

    public SystemException(MessageCode messageCode, Throwable cause) {
        super(cause);
        this.code = messageCode;
    }

    public SystemException(MessageCode messageCode, String message, Throwable cause) {
        super(message, cause);
        this.code = messageCode;
    }

    public MessageCode getCode() {
        return code;
    }

}
