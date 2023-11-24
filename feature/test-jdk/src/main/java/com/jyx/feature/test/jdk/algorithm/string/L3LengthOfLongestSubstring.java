package com.jyx.feature.test.jdk.algorithm.string;

import java.util.HashMap;
import java.util.Map;

/**
 * 力扣 3. 无重复字符的最长子串
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 * <p>
 * 示例 1:
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * <p>
 * 示例 2:
 * 输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * <p>
 * 示例 3:
 * 输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 * 请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 * <p>
 * 提示：
 * 0 <= s.length <= 5 * 104
 * s 由英文字母、数字、符号和空格组成
 *
 * @author jiangyaxin
 * @since 2023/6/1 13:50
 */
public class L3LengthOfLongestSubstring {

    public int lengthOfLongestSubstring(String s) {
        int left = 0;
        int right = 0;
        Map<Character, Integer> window = new HashMap<>();
        int length = 0;


        while (right < s.length()) {
            char rightChar = s.charAt(right);
            Integer count = window.merge(rightChar, 1, (base, incr) -> base + incr);
            right++;
            if (count > 1) {
                length = Math.max(length, right - left - 1);
            } else {
                length = Math.max(length, right - left);
            }

            while (left < right && window.get(rightChar) > 1) {
                char leftChar = s.charAt(left);
                window.put(leftChar, window.get(leftChar) - 1);
                left++;
            }
        }

        return length;
    }

    public int lengthOfLongestSubstring2(String s) {
        int left = 0;
        int right = 0;
        Map<Character, Integer> window = new HashMap<>();
        int length = 0;

        while (right < s.length()) {
            char rightChar = s.charAt(right);
            if (window.containsKey(rightChar) && window.get(rightChar) >= left){
                left = Math.max(window.get(rightChar) + 1, left);
            } else {

                length = Math.max(length, right - left + 1);
            }
            window.put(rightChar, right);
            right++;
        }

        return length;
    }

}
