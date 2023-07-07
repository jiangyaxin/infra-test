package com.jyx.feature.test.jdk.algorithm.dp;

/**
 * 力扣 583. 两个字符串的删除操作
 * 给定两个单词word1和word2，返回使得word1和word2相同所需的最小步数。
 *
 * 每步可以删除任意一个字符串中的一个字符。
 *
 * 示例 1：
 * 输入: word1 = "sea", word2 = "eat"
 * 输出: 2
 * 解释: 第一步将 "sea" 变为 "ea" ，第二步将 "eat "变为 "ea"
 *
 * 示例 2:
 * 输入：word1 = "leetcode", word2 = "etco"
 * 输出：4
 *
 * 提示：
 * 1 <= word1.length, word2.length <= 500
 * word1和word2只包含小写英文字母
 *
 * @author jiangyaxin
 * @since 2022/7/14 20:48
 */
public class L583MinDistance2 {

    public int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        int[][] dp = new int[len1+1][len2+1];

        for(int i = 0; i <= len1 ; i++) {
            dp[i][0] = i;
        }

        for(int j = 0; j <= len2 ; j++) {
            dp[0][j] = j;
        }

        for(int i = 1; i <= len1 ; i++) {
            for(int j = 1; j <= len2 ; j++) {
                if(word1.charAt(i-1) == word2.charAt(j-1)){
                    dp[i][j] = min(dp[i-1][j-1],dp[i-1][j]+1,dp[i][j-1]+1);
                } else {
                    dp[i][j] = min(dp[i-1][j-1]+2,dp[i-1][j]+1,dp[i][j-1]+1);
                }
            }
        }


        return dp[len1][len2];
    }

    private int min(int a, int b, int c){
        return Math.min(a,Math.min(b,c));
    }

}
