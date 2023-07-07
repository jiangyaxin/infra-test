package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.bfs.L111MinDepth;
import com.jyx.feature.test.jdk.algorithm.bfs.L752OpenLock;
import com.jyx.feature.test.jdk.algorithm.bfs.L773SlidingPuzzle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jiangyaxin
 * @since 2022/4/20 20:55
 */
public class BfsTests {

    @Test
    public void minDepthTest() {
        L111MinDepth.TreeNode treeNode = new L111MinDepth.TreeNode(3, new L111MinDepth.TreeNode(9, null, null), new L111MinDepth.TreeNode(20, new L111MinDepth.TreeNode(15, null, null), new L111MinDepth.TreeNode(20, null, null)));
        assertThat(new L111MinDepth().minDepth(treeNode))
                .as("minDepthTest")
                .isEqualTo(2);
    }

    @Test
    public void openLockTest() {
        assertThat(new L752OpenLock().openLock(new String[]{"0201", "0101", "0102", "1212", "2002"}, "0202"))
                .as("openLockTest")
                .isEqualTo(6);

        assertThat(new L752OpenLock().doubleBfs(new String[]{"0201", "0101", "0102", "1212", "2002"}, "0202"))
                .as("openLockTest")
                .isEqualTo(6);
    }

    @Test
    public void slidingPuzzleTest() {
        assertThat(new L773SlidingPuzzle().slidingPuzzle(new int[][]{{1, 2, 3}, {4, 0, 5}}))
                .as("slidingPuzzleTest")
                .isEqualTo(1);
        assertThat(new L773SlidingPuzzle().slidingPuzzle(new int[][]{{1, 2, 3}, {5, 4, 0}}))
                .as("slidingPuzzleTest")
                .isEqualTo(-1);
    }
}
