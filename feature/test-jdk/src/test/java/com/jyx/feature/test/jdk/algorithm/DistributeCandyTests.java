package com.jyx.feature.test.jdk.algorithm;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jiangyaxin
 * @since 2021/1/27 15:36
 */
public class DistributeCandyTests {

    @Test
    public void distributeCandyTest() {
        assertThat(new L162DistributeCandy().distributeCandies(new int[]{1,1,2,2,3,3})).as("DistributeCandyTests").isEqualTo(3);
        assertThat(new L162DistributeCandy().distributeCandies(new int[]{1,1,2,3})).as("DistributeCandyTests").isEqualTo(2);
    }
}
