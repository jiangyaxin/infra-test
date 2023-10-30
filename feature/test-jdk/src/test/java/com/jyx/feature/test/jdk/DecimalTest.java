package com.jyx.feature.test.jdk;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author jiang
 * @since 2023/7/29 18:06
 */
public class DecimalTest {

    @Test
    public void test() {
        BigDecimal bigDecimal = new BigDecimal("0.00");
        System.out.println(bigDecimal.toBigInteger());
    }
}
