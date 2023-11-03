package com.jyx.infra.spring.pipeline;

import com.jyx.infra.pipeline.disruptor.WaitMode;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Archforce
 * @since 2023/11/3 10:31
 */
@Target({})
@Retention(RUNTIME)
public @interface WaitStrategy {

    WaitMode waitMode() default WaitMode.BLOCKING;

    long spinTimeout() default 0;

    long timeout() default 0;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    WaitMode fallbackWaitMode() default WaitMode.BLOCKING;

}
