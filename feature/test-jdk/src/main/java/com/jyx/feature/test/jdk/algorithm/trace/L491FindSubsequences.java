package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 力扣 491. 递增子序列
 * 给你一个整数数组 nums ，找出并返回所有该数组中不同的递增子序列，递增子序列中 至少有两个元素 。你可以按 任意顺序 返回答案。
 *
 * 数组中可能含有重复元素，如出现两个整数相等，也可以视作递增序列的一种特殊情况。
 *
 * 示例 1：
 * 输入：nums = [4,6,7,7]
 * 输出：[[4,6],[4,6,7],[4,6,7,7],[4,7],[4,7,7],[6,7],[6,7,7],[7,7]]
 *
 * 示例 2：
 * 输入：nums = [4,4,3,2,1]
 * 输出：[[4,4]]
 *
 * 提示：
 * 1 <= nums.length <= 15
 * -100 <= nums[i] <= 100
 *
 * @author jiangyaxin
 * @since 2022/4/16 20:50
 */
public class L491FindSubsequences {

    List<List<Integer>> result = new ArrayList<>();

    public List<List<Integer>> findSubsequences(int[] nums) {
        List<Integer> path = new ArrayList<>();
        findSubsequences(path,0,nums);
        return result;
    }

    public void findSubsequences(List<Integer> throughPath,int start,int[] nums) {
        if(start == nums.length){
            return;
        }

        Set<Integer> selectInCurrentLevelSet = new HashSet<>();
        for(int i = start ; i < nums.length ; i++){
            int num = nums[i];
            if (selectInCurrentLevelSet.contains(num)){
                continue;
            }
            if(throughPath.size() >0 && num < throughPath.get(throughPath.size() -1)){
                continue;
            }

            throughPath.add(num);
            if(throughPath.size() > 1){
                result.add(new ArrayList<>(throughPath));
            }
            selectInCurrentLevelSet.add(num);
            findSubsequences(throughPath,i+1,nums);
            throughPath.remove(throughPath.size()-1);
        }
    }

}
