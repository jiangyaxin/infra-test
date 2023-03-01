package com.jyx.infra.web.exception.handler;

import com.jyx.infra.exception.BusinessException;
import com.jyx.infra.web.result.ErrorResult;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

import static com.jyx.infra.web.exception.WebMessageCodes.WRONG_PARAMETER_CODE;
import static com.jyx.infra.web.exception.handler.ExceptionHandlerOrders.VALIDATION_EXCEPTION_HANDLER_ORDER;

/**
 * @author JYX
 * @since 2021/11/5 17:44
 */
@Order(VALIDATION_EXCEPTION_HANDLER_ORDER)
@ControllerAdvice
@Component
public class ValidationExceptionHandler {

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResult> handle(Exception cause) {
        BusinessException businessException = ValidationExceptionHandlerSupport.transferValidationException(cause);
        return DefaultExceptionHandler.handleBadRequest(businessException, WRONG_PARAMETER_CODE);
    }
}
