package com.jyx.infra.web.exception.handler;

import org.springframework.core.Ordered;

/**
 * @author JYX
 * @since 2021/11/5 17:30
 */
public interface ExceptionHandlerOrders {

    int VALIDATION_EXCEPTION_HANDLER_ORDER = 95;

    int BUSINESS_EXCEPTION_HANDLER_ORDER = 100;

    int DEFAULT_EXCEPTION_HANDLER_ORDER = Ordered.LOWEST_PRECEDENCE - 1;
}
