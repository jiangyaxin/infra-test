package com.jyx.feature.test.jdk.algorithm.dp;

/**
 * 力扣 53. 最大子数组和
 * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 *
 * 子数组 是数组中的一个连续部分。
 *
 * 示例 1：
 * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
 * 输出：6
 * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。
 *
 * 示例 2：
 * 输入：nums = [1]
 * 输出：1
 *
 * 示例 3：
 * 输入：nums = [5,4,-1,7,8]
 * 输出：23
 *
 * 提示：
 * 1 <= nums.length <= 105
 * -104 <= nums[i] <= 104
 *
 * @author jiangyaxin
 * @since 2022/7/13 20:08
 */
public class L53MaxSubArray {

    public int maxSubArray(int[] nums) {
        int len = nums.length;

        int[] dp = new int[len];
        dp[0] = nums[0];
        if(len == 1){
            return dp[len-1];
        }

        for( int i = 1 ; i < len ; i++ ){
            dp[i] = Math.max(dp[i-1]+nums[i],nums[i]);
        }

        int result = dp[0];

        for( int i = 1 ; i < len ; i++ ){
            result = Math.max(result,dp[i]);
        }
        return result;
    }

}
