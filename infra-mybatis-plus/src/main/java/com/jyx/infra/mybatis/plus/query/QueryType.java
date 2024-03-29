package com.jyx.infra.mybatis.plus.query;

import java.lang.annotation.*;

/**
 * @author jiangyaxin
 * @since 2023/10/30 14:42
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryType {

    QueryTypes value() default QueryTypes.EQ;

    String entityFieldName() default "";

    BetweenType betweenType() default BetweenType.DEFAULT;
}
