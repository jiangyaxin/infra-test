package com.jyx.infra.web.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jyx.infra.util.ExceptionUtil;
import com.jyx.infra.exception.MessageCode;
import com.jyx.infra.spring.context.AppConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author JYX
 * @since 2021/11/5 16:32
 */
@Data
@NoArgsConstructor
@JsonPropertyOrder({"module", "code", "message", "trace"})
public class ErrorResult {

    private String module = AppConstant.MODULE;

    @JsonIgnore
    private MessageCode messageCode;

    @JsonIgnore
    private Throwable cause;

    private Integer code;

    private String message;

    private List<String> trace;

    public ErrorResult(MessageCode messageCode, Throwable cause) {
        this.messageCode = messageCode;
        this.cause = cause;
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
        this.trace = ExceptionUtil.getTrace(cause);
    }
}
