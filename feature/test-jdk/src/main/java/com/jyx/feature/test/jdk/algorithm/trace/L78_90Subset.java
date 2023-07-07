package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.*;

/**
 * @author jiangyaxin
 * @since 2022/4/13 20:48
 */
public class L78_90Subset {

    List<List<Integer>> result = new ArrayList<>();

    /**
     * 力扣 78. 子集
     * 给你一个整数数组 nums ，数组中的元素 互不相同 。返回该数组所有可能的子集（幂集）。
     *
     * 解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。
     *
     * 示例 1：
     * 输入：nums = [1,2,3]
     * 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
     *
     * 示例 2：
     * 输入：nums = [0]
     * 输出：[[],[0]]
     *
     * 提示：
     * 1 <= nums.length <= 10
     * -10 <= nums[i] <= 10
     * nums 中的所有元素 互不相同
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<Integer> path = new ArrayList<>();
        result.add(path);
        subsets(path,0,nums);
        return result;
    }

    public void subsets(List<Integer> throughPath,int start,int[] nums) {

        if(start == nums.length){
            return;
        }

        for(int i=start ; i < nums.length ; i++){
            throughPath.add(nums[i]);
            result.add(new ArrayList<>(throughPath));
            subsets(throughPath,i+1,nums);
            throughPath.remove(throughPath.size()-1);
        }
    }

    /**
     * 力扣 90. 子集 II
     * 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
     *
     * 解集 不能 包含重复的子集。返回的解集中，子集可以按 任意顺序 排列。
     *
     * 示例 1：
     * 输入：nums = [1,2,2]
     * 输出：[[],[1],[1,2],[1,2,2],[2],[2,2]]
     *
     * 示例 2：
     * 输入：nums = [0]
     * 输出：[[],[0]]
     *
     *
     * 提示：
     * 1 <= nums.length <= 10
     * -10 <= nums[i] <= 10
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<Integer> path = new ArrayList<>();
        result.add(path);
        Arrays.sort(nums);
        subsetsWithDup(path,0,nums);
        return result;
    }

    public void subsetsWithDup(List<Integer> throughPath,int start,int[] nums) {

        if (start == nums.length) {
            return;
        }

        Set<Integer> selectInCurrentLevelSet = new HashSet<>();
        for (int i = start; i < nums.length; i++) {
            int num = nums[i];
            if (selectInCurrentLevelSet.contains(num)) {
                continue;
            }
            throughPath.add(num);
            selectInCurrentLevelSet.add(num);
            result.add(new ArrayList<>(throughPath));
            subsetsWithDup(throughPath, i + 1, nums);
            throughPath.remove(throughPath.size() - 1);
        }
    }
}
