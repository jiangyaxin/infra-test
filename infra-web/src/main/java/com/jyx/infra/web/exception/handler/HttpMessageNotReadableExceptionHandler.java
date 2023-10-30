package com.jyx.infra.web.exception.handler;

import com.jyx.infra.web.result.ErrorResult;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.jyx.infra.exception.WebMessageCodes.WRONG_PARAMETER_CODE;
import static com.jyx.infra.web.exception.handler.ExceptionHandlerOrders.HTTP_MESSAGE_NOT_READABLE_EXCEPTION_ORDER;

/**
 * @author asa
 * @since 2021/11/7 19:05
 */
@Order(HTTP_MESSAGE_NOT_READABLE_EXCEPTION_ORDER)
@ControllerAdvice
@Component
public class HttpMessageNotReadableExceptionHandler {

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResult> handle(HttpMessageNotReadableException e) {
        return DefaultExceptionHandler.handleBadRequest(e, WRONG_PARAMETER_CODE);
    }
}
