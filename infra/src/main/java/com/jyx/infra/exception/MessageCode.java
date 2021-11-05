package com.jyx.infra.exception;

import lombok.Getter;
import lombok.Value;

/**
 * @author JYX
 * @since 2021/11/5 15:45
 */
@Getter
@Value(staticConstructor = "of")
public class MessageCode {

    Integer code;

    String message;
}
