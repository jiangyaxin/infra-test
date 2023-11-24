package com.jyx.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

/**
 * @author jiangyaxin
 * @since 2021/11/5 15:45
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class MessageCode {

    Integer code;

    String message;
}
