package com.jyx.feature.test.jdk.algorithm;

/**
 * 力扣 162. 寻找峰值
 *
 * 峰值元素是指其值大于左右相邻值的元素。
 *
 * 给你一个输入数组nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。
 *
 * 你可以假设nums[-1] = nums[n] = -∞ 。
 *
 * 
 *
 * 示例 1：
 *
 * 输入：nums = [1,2,3,1]
 * 输出：2
 * 解释：3 是峰值元素，你的函数应该返回其索引 2。
 * 示例2：
 *
 * 输入：nums = [1,2,1,3,5,6,4]
 * 输出：1 或 5
 * 解释：你的函数可以返回索引 1，其峰值元素为 2；
 *     或者返回索引 5， 其峰值元素为 6。
 * 
 *
 * 提示：
 *
 * 1 <= nums.length <= 1000
 * -231 <= nums[i] <= 231 - 1
 * 对于所有有效的 i 都有 nums[i] != nums[i + 1]
 * 
 *
 * 进阶：你可以实现时间复杂度为 O(logN) 的解决方案吗？
 *
 * 二分法 O(logN)
 * 所以 考虑使用二分法
 * 由于 nums[-1] = nums[n] = -∞ 所以二分之后往大的一方找必存在波峰，即使找不到，找至最后一个数那么最后一个数(0或n-1)就是波峰。
 *
 * @author JYX
 * @since 2021/1/28 16:18
 */
public class L162FindPeakElement {

    public int findPeakElement(int[] nums) {
        if(nums.length == 1){
            return 0;
        }else if(nums.length == 2){
            return nums[0] > nums[1] ? 0 : 1;
        }
        int left = 0;
        int right = nums.length-1;

        while (left < right){
            int mid = getMid(left,right);
            if(mid+1 > nums.length-1){
                return mid;
            }
            if( nums[mid] < nums[mid+1]){
                left = mid+1;
            }else {
                if(mid-1 >=0 && nums[mid] < nums[mid-1]){
                    right = mid-1;
                }else {
                    return mid;
                }

            }
        }

        return left;
    }

    /**
     * 防止溢出, left+ (right/2 - left/2) = (right/2 + left/2)
     */
    private int getMid(int left, int right){
        return left + ((right - left) >> 1);
    }
}
