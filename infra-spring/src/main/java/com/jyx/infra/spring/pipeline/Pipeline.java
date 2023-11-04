package com.jyx.infra.spring.pipeline;

import com.jyx.infra.pipeline.disruptor.WaitMode;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Archforce
 * @since 2023/11/2 15:44
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Pipeline {

    boolean enable() default true;

    String name() default "";

    int bufferSize() default 1024;

    long stopTimeout() default -1;

    TimeUnit stopTimeUnit() default TimeUnit.MILLISECONDS;

    WaitStrategy waitStrategy() default @WaitStrategy(waitMode = WaitMode.BLOCKING);
}
