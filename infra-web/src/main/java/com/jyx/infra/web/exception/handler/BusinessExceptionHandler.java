package com.jyx.infra.web.exception.handler;

import com.jyx.infra.exception.BusinessException;
import com.jyx.infra.web.result.ErrorResult;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.jyx.infra.web.exception.handler.ExceptionHandlerOrders.BUSINESS_EXCEPTION_HANDLER_ORDER;

/**
 * @author JYX
 * @since 2021/11/5 17:41
 */
@Order(BUSINESS_EXCEPTION_HANDLER_ORDER)
@ControllerAdvice
@Component
public class BusinessExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResult> handle(BusinessException e) {
        return DefaultExceptionHandler.handleBadRequest(e, e.getCode());
    }
}
