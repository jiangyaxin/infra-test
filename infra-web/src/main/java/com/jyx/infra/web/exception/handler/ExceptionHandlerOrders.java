package com.jyx.infra.web.exception.handler;

import org.springframework.core.Ordered;

/**
 * @author jiangyaxin
 * @since 2021/11/5 17:30
 */
public interface ExceptionHandlerOrders {

    int HTTP_MESSAGE_NOT_READABLE_EXCEPTION_ORDER = 90;

    int VALIDATION_EXCEPTION_HANDLER_ORDER = 95;

    int OBJECT_OPTIMISTIC_LOCKING_FAILURE_EXCEPTION_HANDLER_ORDER = 96;

    int BUSINESS_EXCEPTION_HANDLER_ORDER = 100;

    int DEFAULT_EXCEPTION_HANDLER_ORDER = Ordered.LOWEST_PRECEDENCE - 1;
}
