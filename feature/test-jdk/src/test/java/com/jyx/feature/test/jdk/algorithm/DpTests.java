package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.dp.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jiangyaxin
 * @since 2022/7/8 15:17
 */
public class DpTests {

    @Test
    public void coinChangeTest(){
        assertThat(new L32CoinChange().coinChange(new int[]{1,2,5},11))
                .as("coinChangeTest")
                .isEqualTo(3);
        assertThat(new L32CoinChange().coinChange(new int[]{1},0))
                .as("coinChangeTest")
                .isEqualTo(0);
    }

    @Test
    public void lengthOfLISTest(){
        assertThat(new L300LengthOfLIS().lengthOfLIS(new int[]{10,9,2,5,3,7,101,18}))
                .as("lengthOfLISTest")
                .isEqualTo(4);
        assertThat(new L300LengthOfLIS().lengthOfLIS(new int[]{1,3,6,7,9,4,10,5,6}))
                .as("lengthOfLISTest")
                .isEqualTo(6);
    }

    @Test
    public void maxEnvelopesTest(){
        assertThat(new L354MaxEnvelopes().maxEnvelopes(new int[][]{
                    {5,4},
                    {6,4},
                    {6,7},
                    {2,3}
                }))
                .as("maxEnvelopesTest")
                .isEqualTo(3);
    }

    @Test
    public void minFallingPathSumTest(){
        assertThat(new L931MinFallingPathSum().minFallingPathSum(new int[][]{
                {2,1,3},
                {6,5,4},
                {7,8,9}
        }))
                .as("minFallingPathSumTest")
                .isEqualTo(13);
    }

    @Test
    public void minDistanceTest(){
        assertThat(new L72MinDistance().minDistance("horse","ros"))
                .as("minDistanceTest")
                .isEqualTo(3);
        assertThat(new L72MinDistance().minDistance("sea","eat"))
                .as("minDistanceTest")
                .isEqualTo(2);
        assertThat(new L72MinDistance().minDistance("intention","execution"))
                .as("minDistanceTest")
                .isEqualTo(5);
        assertThat(new L72MinDistance().minDistance("a","a"))
                .as("minDistanceTest")
                .isEqualTo(0);
        assertThat(new L72MinDistance().minDistance("park","spake"))
                .as("minDistanceTest")
                .isEqualTo(3);
    }

    @Test
    public void maxSubArrayTest(){
        assertThat(new L53MaxSubArray().maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}))
                .as("maxSubArrayTest")
                .isEqualTo(6);
    }

    @Test
    public void longestCommonSubsequenceTest(){
        assertThat(new L1143LongestCommonSubsequence().longestCommonSubsequence("abcde","ace"))
                .as("longestCommonSubsequenceTest")
                .isEqualTo(3);
    }

    @Test
    public void minDistance2Test(){
        assertThat(new L583MinDistance2().minDistance("leetcode","etco"))
                .as("minDistance2Test")
                .isEqualTo(4);
    }

    @Test
    public void minimumDeleteSumTest(){
        assertThat(new L712MinimumDeleteSum().minimumDeleteSum("sea","eat"))
                .as("minimumDeleteSumTest")
                .isEqualTo(231);
    }

}
