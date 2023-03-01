package com.jyx.infra.web.exception;

import com.jyx.infra.exception.MessageCode;

/**
 * @author JYX
 * @since 2021/11/5 17:23
 */
public interface WebMessageCodes {

    MessageCode SERVER_ERROR_CODE = MessageCode.of(50000, "系统错误,请联系管理员!");

    MessageCode WRONG_PARAMETER_CODE = MessageCode.of(40000, "参数错误!");

    MessageCode OBJECT_LOCKING_CODE = MessageCode.of(40001, "数据被其他用户修改，请刷新页面重新操作!");
}
