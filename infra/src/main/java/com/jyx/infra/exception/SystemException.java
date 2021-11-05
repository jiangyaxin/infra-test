package com.jyx.infra.exception;

/**
 * 带 code 的系统异常
 * @author JYX
 * @since 2021/11/5 15:01
 */
public class SystemException extends AppException {

    private final MessageCode code;

    public SystemException(MessageCode messageCode){
        super();
        this.code = messageCode;
    }

    public SystemException(MessageCode messageCode,String message){
        super(message);
        this.code = messageCode;
    }

    public SystemException(MessageCode messageCode,Throwable cause){
        super(cause);
        this.code = messageCode;
    }

    public SystemException(MessageCode messageCode,String message, Throwable cause){
        super(message,cause);
        this.code = messageCode;
    }

    public MessageCode getCode() {
        return code;
    }

}
