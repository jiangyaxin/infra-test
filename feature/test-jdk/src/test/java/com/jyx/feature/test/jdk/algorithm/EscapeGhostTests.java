package com.jyx.feature.test.jdk.algorithm;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author JYX
 * @since 2021/1/27 17:34
 */
public class EscapeGhostTests {

    @Test
    public void escapeGhostTest() {
        assertThat(new L789EscapeGhost().escapeGhosts(new int[][]{{1,0},{0,3}},new int[]{0,1})).as("EscapeGhostTests").isEqualTo(Boolean.TRUE);
        assertThat(new L789EscapeGhost().escapeGhosts(new int[][]{{1,0}},new int[]{2,0})).as("EscapeGhostTests").isEqualTo(Boolean.FALSE);
        assertThat(new L789EscapeGhost().escapeGhosts(new int[][]{{2,0}},new int[]{1,0})).as("EscapeGhostTests").isEqualTo(Boolean.FALSE);
        assertThat(new L789EscapeGhost().escapeGhosts(new int[][]{{5,0},{-10,-2},{0,-5},{-2,-2},{-7,1}},new int[]{7,7})).as("EscapeGhostTests").isEqualTo(Boolean.FALSE);
        assertThat(new L789EscapeGhost().escapeGhosts(new int[][]{{-1,0},{0,1},{-1,0},{0,1},{-1,0}},new int[]{0,0})).as("EscapeGhostTests").isEqualTo(Boolean.TRUE);
    }
}
