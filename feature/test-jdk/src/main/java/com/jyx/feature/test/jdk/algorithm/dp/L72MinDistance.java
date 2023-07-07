package com.jyx.feature.test.jdk.algorithm.dp;

/**
 * 力扣 72. 编辑距离
 * 给你两个单词 word1 和 word2， 请返回将 word1 转换成 word2 所使用的最少操作数  。
 *
 * 你可以对一个单词进行如下三种操作：
 *
 * 插入一个字符
 * 删除一个字符
 * 替换一个字符
 *
 * 示例 1：
 * 输入：word1 = "horse", word2 = "ros"
 * 输出：3
 * 解释：
 * horse -> rorse (将 'h' 替换为 'r')
 * rorse -> rose (删除 'r')
 * rose -> ros (删除 'e')
 *
 * 示例 2：
 * 输入：word1 = "intention", word2 = "execution"
 * 输出：5
 * 解释：
 * intention -> inention (删除 't')
 * inention -> enention (将 'i' 替换为 'e')
 * enention -> exention (将 'n' 替换为 'x')
 * exention -> exection (将 'n' 替换为 'c')
 * exection -> execution (插入 'u')
 *
 * 提示：
 * 0 <= word1.length, word2.length <= 500
 * word1 和 word2 由小写英文字母组成
 *
 * @author jiangyaxin
 * @since 2022/7/12 21:24
 */
public class L72MinDistance {

    public int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        if(len1 == 0){
            return len2;
        }

        if(len2 == 0){
            return len1;
        }

        int[][] dp = new int[len1][len2];

        if(word1.charAt(0) == word2.charAt(0)){
            dp[0][0] = 0;
        } else {
            dp[0][0] = 1;
        }

        for(int i = 1 ; i < len1 ; i++ ){
            String tempWord1 = word1;
            if( i < len1 - 1){
                tempWord1 = word1.substring(0,i + 1);
            }
            if(tempWord1.contains(String.valueOf(word2.charAt(0)))){
                dp[i][0] = i;
            } else {
                dp[i][0] = i + 1;
            }
        }

        for(int j = 1 ; j < len2 ; j++ ){
            String tempWord2 = word2;
            if( j < len2 - 1){
                tempWord2 = word2.substring(0,j + 1);
            }
            if(tempWord2.contains(String.valueOf(word1.charAt(0)))){
                dp[0][j] = j;
            } else {
                dp[0][j] = j + 1;
            }
        }

        for( int i = 1 ; i < len1 ; i++ ){
            for( int j = 1 ; j < len2 ; j++ ){
                if( word1.charAt(i) == word2.charAt(j) ){
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    dp[i][j] = min(dp[i-1][j],dp[i][j-1],dp[i-1][j-1]) + 1;
                }
            }
        }

        return dp[len1-1][len2-1];
    }

    private int min(int a, int b, int c){
        return Math.min(a,Math.min(b,c));
    }
}
