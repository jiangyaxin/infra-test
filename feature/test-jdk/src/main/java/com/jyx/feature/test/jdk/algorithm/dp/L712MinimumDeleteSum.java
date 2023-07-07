package com.jyx.feature.test.jdk.algorithm.dp;

/**
 * 力扣 712. 两个字符串的最小ASCII删除和
 * 给定两个字符串s1 和 s2，返回 使两个字符串相等所需删除字符的 ASCII 值的最小和 。
 *
 * 示例 1:
 * 输入: s1 = "sea", s2 = "eat"
 * 输出: 231
 * 解释: 在 "sea" 中删除 "s" 并将 "s" 的值(115)加入总和。
 * 在 "eat" 中删除 "t" 并将 116 加入总和。
 * 结束时，两个字符串相等，115 + 116 = 231 就是符合条件的最小和。
 *
 * 示例 2:
 * 输入: s1 = "delete", s2 = "leet"
 * 输出: 403
 * 解释: 在 "delete" 中删除 "dee" 字符串变成 "let"，
 * 将 100[d]+101[e]+101[e] 加入总和。在 "leet" 中删除 "e" 将 101[e] 加入总和。
 * 结束时，两个字符串都等于 "let"，结果即为 100+101+101+101 = 403 。
 * 如果改为将两个字符串转换为 "lee" 或 "eet"，我们会得到 433 或 417 的结果，比答案更大。
 *
 * 提示:
 * 0 <= s1.length, s2.length <= 1000
 * s1 和 s2 由小写英文字母组成
 *
 * @author jiangyaxin
 * @since 2022/7/14 21:41
 */
public class L712MinimumDeleteSum {

    public int minimumDeleteSum(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        int[][] dp = new int[len1+1][len2+1];
        dp[0][0] = 0;

        for(int i = 1; i <= len1 ; i++) {
            dp[i][0] = dp[i-1][0] + Integer.valueOf(s1.charAt(i-1));
        }

        for(int j = 1; j <= len2 ; j++) {
            dp[0][j] = dp[0][j-1] + Integer.valueOf(s2.charAt(j-1));
        }

        for(int i = 1; i <= len1 ; i++) {
            for(int j = 1; j <= len2 ; j++) {
                if(s1.charAt(i-1) == s2.charAt(j-1)){
                    dp[i][j] = min(dp[i-1][j-1],dp[i-1][j]+Integer.valueOf(s1.charAt(i-1)),dp[i][j-1]+Integer.valueOf(s2.charAt(j-1)));
                } else {
                    dp[i][j] = min(
                            dp[i-1][j-1]+Integer.valueOf(s1.charAt(i-1))+Integer.valueOf(s2.charAt(j-1)),
                            dp[i-1][j]+Integer.valueOf(s1.charAt(i-1)),
                            dp[i][j-1]+Integer.valueOf(s2.charAt(j-1)));
                }
            }
        }


        return dp[len1][len2];
    }

    private int min(int a, int b, int c){
        return Math.min(a,Math.min(b,c));
    }

}
