package com.jyx.infra.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jyx.infra.exception.Exceptions;
import com.jyx.infra.exception.MessageCode;
import lombok.Data;

import java.util.List;

import static com.jyx.infra.context.AppConstant.MODULE;

/**
 * @author JYX
 * @since 2021/11/5 16:32
 */
@Data
@JsonPropertyOrder({"module", "code", "message", "trace" })
public class ErrorResult {

    private String module = MODULE;

    @JsonIgnore
    private MessageCode messageCode;

    @JsonIgnore
    private Throwable cause;

    private Integer code;

    private String message;

    private List<String> trace;

    public ErrorResult(MessageCode messageCode,Throwable cause){
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
        this.trace = Exceptions.getTrace(cause);
    }
}
