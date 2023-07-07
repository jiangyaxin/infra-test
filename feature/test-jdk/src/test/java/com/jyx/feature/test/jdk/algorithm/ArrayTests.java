package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.array.*;
import com.jyx.feature.test.jdk.algorithm.string.L344ReverseString;
import com.jyx.feature.test.jdk.algorithm.string.L3LengthOfLongestSubstring;
import com.jyx.feature.test.jdk.algorithm.string.L5LongestPalindrome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jiangyaxin
 * @since 2022/4/26 21:08
 */
public class ArrayTests {

    @Test
    public void lengthOfLongestSubstringTest() {
        assertThat(new L3LengthOfLongestSubstring().lengthOfLongestSubstring("abcabcbb")).as("lengthOfLongestSubstringTest")
                .isEqualTo(3);
        assertThat(new L3LengthOfLongestSubstring().lengthOfLongestSubstring("bbbbb")).as("lengthOfLongestSubstringTest")
                .isEqualTo(1);
        assertThat(new L3LengthOfLongestSubstring().lengthOfLongestSubstring("pwwkew")).as("lengthOfLongestSubstringTest")
                .isEqualTo(3);
        assertThat(new L3LengthOfLongestSubstring().lengthOfLongestSubstring("tmmzuxt")).as("lengthOfLongestSubstringTest")
                .isEqualTo(5);
    }

    @Test
    public void longestPalindromeTest() {
        assertThat(new L5LongestPalindrome().longestPalindrome("babad")).as("longestPalindromeTest")
                .isEqualTo("bab");
        assertThat(new L5LongestPalindrome().longestPalindrome("cbbd")).as("longestPalindromeTest")
                .isEqualTo("bb");
    }


    @Test
    public void reverseStringTest() {
        char[] char1 = new char[]{'h', 'e', 'l', 'l', 'o'};
        new L344ReverseString().reverseString(char1);
        char[] target1 = new char[]{'o', 'l', 'l', 'e', 'h'};
        assertThat(char1).as("reverseStringTest")
                .isEqualTo(target1);
    }


    @Test
    public void twoSumTest() {
        assertThat(new L167TwoSum().twoSum(new int[]{2, 7, 11, 15}, 9)).as("twoSumTest")
                .isEqualTo(new int[]{1, 2});
        assertThat(new L167TwoSum().twoSum(new int[]{2, 3, 4}, 6)).as("twoSumTest")
                .isEqualTo(new int[]{1, 3});
        assertThat(new L167TwoSum().twoSum(new int[]{-1, 0}, -1)).as("twoSumTest")
                .isEqualTo(new int[]{1, 2});
    }

    @Test
    public void removeElementTest() {
        assertThat(new L27RemoveElement().removeElement(new int[]{3, 2, 2, 3}, 3)).as("removeElementTest")
                .isEqualTo(2);
        assertThat(new L27RemoveElement().removeElement(new int[]{0, 1, 2, 2, 3, 0, 4, 2}, 2)).as("removeElementTest")
                .isEqualTo(5);
    }

    @Test
    public void moveZeroesTest() {
        int[] nums1 = {0, 1, 0, 3, 12};
        new L283MoveZeroes().moveZeroes(nums1);
        int[] target1 = {1, 3, 12, 0, 0};
        assertThat(nums1).as("moveZeroesTest")
                .isEqualTo(target1);

        int[] nums2 = {0};
        new L283MoveZeroes().moveZeroes(nums2);
        int[] target2 = {0};
        assertThat(nums2).as("moveZeroesTest")
                .isEqualTo(target2);

        int[] nums3 = {2, 1};
        new L283MoveZeroes().moveZeroes(nums3);
        int[] target3 = {2, 1};
        assertThat(nums3).as("moveZeroesTest")
                .isEqualTo(target3);
    }

    @Test
    public void numArrayTest() {
        L303NumArray l303NumArray = new L303NumArray(new int[]{-2, 0, 3, -5, 2, -1});
        assertThat(l303NumArray.sumRange(0, 2))
                .as("numArrayTest")
                .isEqualTo(1);
        assertThat(l303NumArray.sumRange(2, 5))
                .as("numArrayTest")
                .isEqualTo(-1);
        assertThat(l303NumArray.sumRange(0, 5))
                .as("numArrayTest")
                .isEqualTo(-3);
    }

    @Test
    public void numMatrixTest() {
        L304NumMatrix l304NumMatrix = new L304NumMatrix(new int[][]{{3, 0, 1, 4, 2}, {5, 6, 3, 2, 1}, {1, 2, 0, 1, 5}, {4, 1, 0, 1, 7}, {1, 0, 3, 0, 5}});
        assertThat(l304NumMatrix.sumRegion(2, 1, 4, 3))
                .as("numMatrixTest")
                .isEqualTo(8);
        assertThat(l304NumMatrix.sumRegion(1, 1, 2, 2))
                .as("numMatrixTest")
                .isEqualTo(11);
        assertThat(l304NumMatrix.sumRegion(1, 2, 2, 4))
                .as("numMatrixTest")
                .isEqualTo(12);
    }

    @Test
    public void corpFlightBookingsTest() {
        assertThat(new L1109CorpFlightBookings().corpFlightBookings(new int[][]{{1, 2, 10}, {2, 3, 20}, {2, 5, 25}}, 5))
                .as("corpFlightBookingsTest")
                .isEqualTo(new int[]{10, 55, 45, 25, 25});
    }

    @Test
    public void carPoolingTest() {
        assertThat(new L1094CarPooling().carPooling(new int[][]{{2, 1, 5}, {3, 5, 7}}, 3))
                .as("carPoolingTest")
                .isEqualTo(true);
        assertThat(new L1094CarPooling().carPooling(new int[][]{{9, 0, 1}, {3, 3, 7}}, 4))
                .as("carPoolingTest")
                .isEqualTo(false);
    }

    @Test
    public void removeDuplicatesTest() {
        assertThat(new L26RemoveDuplicates().removeDuplicates(new int[]{1, 1, 2}))
                .as("removeDuplicatesTest")
                .isEqualTo(2);
    }
}
