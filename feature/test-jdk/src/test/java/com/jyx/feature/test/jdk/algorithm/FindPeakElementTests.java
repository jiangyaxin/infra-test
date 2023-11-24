package com.jyx.feature.test.jdk.algorithm;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jiangyaxin
 * @since 2021/1/28 17:22
 */
public class FindPeakElementTests {

    @Test
    public void findPeakElementTest() {
        assertThat(new L162FindPeakElement().findPeakElement(new int[]{1})).as("findPeakElementTest").isEqualTo(0);
        assertThat(new L162FindPeakElement().findPeakElement(new int[]{1,2})).as("findPeakElementTest").isEqualTo(1);
        assertThat(new L162FindPeakElement().findPeakElement(new int[]{2,1})).as("findPeakElementTest").isEqualTo(0);
        assertThat(new L162FindPeakElement().findPeakElement(new int[]{1,2,3,1})).as("findPeakElementTest").isEqualTo(2);
        assertThat(new L162FindPeakElement().findPeakElement(new int[]{1,2,1,3,5,6,4})).as("findPeakElementTest").has(anyOf(new Condition<>( (Integer result) -> result.equals(1),"结果不为1" ),new Condition<>( (Integer result) -> result.equals(5),"结果不为5" )));
        assertThat(new L162FindPeakElement().findPeakElement(new int[]{5,4,3,2,1})).as("findPeakElementTest").isEqualTo(0);
        assertThat(new L162FindPeakElement().findPeakElement(new int[]{1,2,3,4,5})).as("findPeakElementTest").isEqualTo(4);
    }

}
