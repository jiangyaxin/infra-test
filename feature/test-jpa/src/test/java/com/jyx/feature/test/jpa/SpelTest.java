package com.jyx.feature.test.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author jiangyaxin
 * @since 2022/10/14 15:36
 */
public class SpelTest {

    @Test
    public void numberPlusTest(){

        SpelExpressionParser parser = new SpelExpressionParser();
        Integer value = parser.parseExpression("(21+3)*4").getValue(Integer.class);
        Assertions.assertEquals((21+3)*4,value);
    }
}
