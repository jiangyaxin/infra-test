package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.Arrays;

/**
 * 力扣 698. 划分为k个相等的子集
 * 给定一个整数数组  nums 和一个正整数 k，找出是否有可能把这个数组分成 k 个非空子集，其总和都相等。
 *
 * 示例 1：
 * 输入： nums = [4, 3, 2, 3, 5, 2, 1], k = 4
 * 输出： True
 * 说明： 有可能将其分成 4 个子集（5），（1,4），（2,3），（2,3）等于总和。
 *
 * 示例 2:
 * 输入: nums = [1,2,3,4], k = 3
 * 输出: false
 *
 *
 * 提示：
 * 1 <= k <= len(nums) <= 16
 * 0 < nums[i] < 10000
 * 每个元素的频率在 [1,4] 范围内
 *
 * @author jiangyaxin
 * @since 2022/4/18 19:57
 */
public class L698CanPartitionSubset {

    private int originalSubsetSum;

    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = Arrays.stream(nums).reduce(0, Integer::sum);
        if(sum % k != 0){
            return false;
        }
        originalSubsetSum = sum / k;

        boolean[] used = new boolean[nums.length];
        Arrays.sort(nums);

        return canPartitionKSubsets(0,nums,used,0,k,originalSubsetSum);
    }


    public boolean canPartitionKSubsets(int start, int[] nums,boolean[] used ,int usedCount,int k, int subsetSum) {
        if(usedCount == nums.length && k == 0){
            return true;
        }
        if(!(usedCount < nums.length && k > 0)){
            return false;
        }
        for(int i=start ; i < nums.length ; i++){
            if(used[i]){
                continue;
            }
            int num = nums[i];
            if(num > subsetSum){
                return false;
            } else if(num == subsetSum){
                used[i] = true;
                if(canPartitionKSubsets(0,nums,used,usedCount+1,k-1,originalSubsetSum)){
                    return true;
                }
                used[i] = false;
            }else {
                used[i] = true;
                if(canPartitionKSubsets(i+1,nums,used,usedCount+1,k,subsetSum-num)){
                    return true;
                }
                used[i] = false;
            }

        }
        return false;
    }
}
