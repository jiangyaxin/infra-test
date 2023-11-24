package com.jyx.feature.test.jdk.algorithm.dp;

import java.util.Arrays;

/**
 * 力扣 354. 俄罗斯套娃信封问题
 * 给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度。
 * 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
 * 请计算 最多能有多少个 信封能组成一组“俄罗斯套娃”信封（即可以把一个信封放到另一个信封里面）。
 * 注意：不允许旋转信封。
 *
 * 示例 1：
 * 输入：envelopes = [[5,4],[6,4],[6,7],[2,3]]
 * 输出：3
 * 解释：最多信封的个数为 3, 组合为: [2,3] => [5,4] => [6,7]。
 *
 * 示例 2：
 * 输入：envelopes = [[1,1],[1,1],[1,1]]
 * 输出：1
 *
 * 提示：
 * 1 <= envelopes.length <= 105
 * envelopes[i].length == 2
 * 1 <= wi, hi <= 105
 *
 * @author jiangyaxin
 * @since 2022/7/8 17:20
 */
public class L354MaxEnvelopes {

    public int maxEnvelopes(int[][] envelopes) {
        int len = envelopes.length;
        int[] dp = new int[len];

        Arrays.sort(envelopes, (o1, o2) -> (o1[0] - o2[0]) * 10 + o1[1] - o2[1]);

        for(int i = 0 ; i < len ; i++){
            int result = 1;

            for(int y = 0 ; y < i ; y++){
                if(envelopes[y][0] < envelopes[i][0] && envelopes[y][1] < envelopes[i][1]) {
                    result = Math.max(result,dp[y]+1);
                }
            }

            dp[i] = result;
        }

        return Arrays.stream(dp).max().getAsInt();
    }

}
