package com.jyx.infra.web.exception.handler;

import com.jyx.infra.exception.MessageCode;
import com.jyx.infra.exception.SystemException;
import com.jyx.infra.web.result.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.jyx.infra.spring.context.AppConstant.MODULE;
import static com.jyx.infra.exception.WebMessageCodes.SERVER_ERROR_CODE;
import static com.jyx.infra.web.exception.handler.ExceptionHandlerOrders.DEFAULT_EXCEPTION_HANDLER_ORDER;

/**
 * @author JYX
 * @since 2021/11/5 16:28
 */
@Slf4j
@Order(DEFAULT_EXCEPTION_HANDLER_ORDER)
@ControllerAdvice
@Component
public class DefaultExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResult> handleException(Exception e) {
        return handleInternalServerError(e);
    }

    public static ResponseEntity<ErrorResult> handleInternalServerError(Throwable cause) {
        return handleInternalServerError(cause, SERVER_ERROR_CODE);
    }

    public static ResponseEntity<ErrorResult> handleInternalServerError(Throwable cause, MessageCode messageCode) {
        return handle(cause, messageCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<ErrorResult> handleBadRequest(Throwable cause, MessageCode messageCode) {
        return handle(cause, messageCode, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ErrorResult> handle(Throwable cause, MessageCode messageCode, HttpStatus status) {
        logError(cause, messageCode);
        ErrorResult errorResult = new ErrorResult(messageCode, cause);
        return createErrorResponse(status, errorResult);
    }

    private static ResponseEntity<ErrorResult> createErrorResponse(HttpStatus status, ErrorResult errorResult) {
        return ResponseEntity.status(status).body(errorResult);
    }

    private static void logError(Throwable cause, MessageCode messageCode) {
        if (!log.isErrorEnabled()) {
            return;
        }
        if (cause instanceof SystemException) {
            SystemException systemException = (SystemException) cause;
            MessageCode systemExceptionCode = systemException.getCode();
            log.error("[{}]-[{}] -> {}", MODULE, systemExceptionCode.getCode(), systemExceptionCode.getMessage(), systemException);
        } else {
            log.error("[{}]-[{}] -> {}", MODULE, messageCode.getCode(), messageCode.getMessage(), cause);
        }
    }
}
