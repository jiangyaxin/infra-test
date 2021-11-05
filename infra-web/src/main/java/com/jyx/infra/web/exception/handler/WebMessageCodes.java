package com.jyx.infra.web.exception.handler;

import com.jyx.infra.exception.MessageCode;

/**
 * @author JYX
 * @since 2021/11/5 17:23
 */
public interface WebMessageCodes {

    MessageCode SERVER_ERROR_CODE = MessageCode.of(50000, "系统错误,请联系管理员!");

    MessageCode WRONG_PARAMETER_CODE = MessageCode.of(40000, "参数错误!");
}
