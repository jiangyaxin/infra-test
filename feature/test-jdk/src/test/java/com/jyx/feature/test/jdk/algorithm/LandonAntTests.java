package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.ant.LandonAnt;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jiangyaxin
 * @since 2021/1/29 11:03
 */
public class LandonAntTests {

    @Test
    public void landonAntTest(){
        assertThat(new LandonAnt().printKMoves(0)).as("LandonAntTests").isEqualTo(Lists.list("R"));
        assertThat(new LandonAnt().printKMoves(2)).as("LandonAntTests").isEqualTo(Lists.list("_X", "LX"));
        assertThat(new LandonAnt().printKMoves(5)).as("LandonAntTests").isEqualTo(Lists.list("_U", "X_", "XX"));
    }
}
