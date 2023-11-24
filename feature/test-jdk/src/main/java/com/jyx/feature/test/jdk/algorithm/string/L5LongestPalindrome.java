package com.jyx.feature.test.jdk.algorithm.string;

/**
 * 力扣 5. 最长回文子串
 * 给你一个字符串 s，找到 s 中最长的回文子串。
 * 如果字符串的反序与原始字符串相同，则该字符串称为回文字符串。
 * <p>
 * 示例 1：
 * 输入：s = "babad"
 * 输出："bab"
 * 解释："aba" 同样是符合题意的答案。
 * <p>
 * 示例 2：
 * 输入：s = "cbbd"
 * 输出："bb"
 * <p>
 * 提示：
 * 1 <= s.length <= 1000
 * s 仅由数字和英文字母组成
 *
 * @author jiangyaxin
 * @since 2023/5/29 16:08
 */
public class L5LongestPalindrome {

    public String longestPalindrome(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            String oddPalindrome = palindromeLength(s, i, i);
            if (oddPalindrome.length() > result.length()) {
                result = oddPalindrome;
            }
            if (i + 1 < s.length() && s.charAt(i) == s.charAt(i + 1)) {
                String evenPalindrome = palindromeLength(s, i, i + 1);
                if (evenPalindrome.length() > result.length()) {
                    result = evenPalindrome;
                }
            }
        }
        return result;
    }

    private String palindromeLength(String s, int left, int right) {
        while (left >= 0 && right < s.length()) {
            if (s.charAt(left) != s.charAt(right)) {
                break;
            } else {
                left--;
                right++;
            }
        }

        if (right < s.length()) {
            return s.substring(left + 1, right);
        } else {
            return s.substring(left + 1);
        }
    }

}
