package com.jyx.infra.spring.pipeline;

import java.lang.annotation.*;

/**
 * @author jiangyaxin
 * @since 2023/11/2 15:49
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Stage {

    String name() default "";

    int parallel() default 1;

    int order() default 5;

}
