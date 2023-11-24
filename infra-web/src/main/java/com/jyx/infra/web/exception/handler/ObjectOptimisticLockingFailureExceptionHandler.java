package com.jyx.infra.web.exception.handler;

import com.jyx.infra.exception.BusinessException;
import com.jyx.infra.web.result.ErrorResult;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.jyx.infra.exception.WebMessageCodes.OBJECT_LOCKING_CODE;
import static com.jyx.infra.web.exception.handler.ExceptionHandlerOrders.OBJECT_OPTIMISTIC_LOCKING_FAILURE_EXCEPTION_HANDLER_ORDER;

/**
 * @author jiangyaxin
 * @since 2021/11/6 0:47
 */
@Order(OBJECT_OPTIMISTIC_LOCKING_FAILURE_EXCEPTION_HANDLER_ORDER)
@ControllerAdvice
@Component
public class ObjectOptimisticLockingFailureExceptionHandler {

    @ExceptionHandler(value = {ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<ErrorResult> handle(ObjectOptimisticLockingFailureException cause) {
        BusinessException businessException = BusinessException.of(OBJECT_LOCKING_CODE, cause);
        return DefaultExceptionHandler.handleBadRequest(businessException, OBJECT_LOCKING_CODE);
    }
}
