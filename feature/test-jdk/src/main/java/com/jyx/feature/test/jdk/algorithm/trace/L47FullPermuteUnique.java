package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 力扣 47. 全排列2
 * 给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
 *
 * 示例 1：
 * 输入：nums = [1,1,2]
 * 输出：
 * [[1,1,2],
 *  [1,2,1],
 *  [2,1,1]]
 *
 * 示例 2：
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 *
 * 提示：
 * 1 <= nums.length <= 8
 * -10 <= nums[i] <= 10
 *
 * @author jiangyaxin
 * @since 2022/4/11 21:16
 */
public class L47FullPermuteUnique {

    List<List<Integer>> result = new ArrayList<>();

    public List<List<Integer>> permuteUnique(int[] nums) {
        int numLength = nums.length;
        List<Integer> path = new ArrayList<>(numLength);
        boolean[] used = new boolean[numLength];

        permute(path,nums, used);
        return result;
    }

    private void permute(List<Integer> throughPath,int[] remainNums,boolean[] used){
        // 临界值,最后一个节点
        if(throughPath.size() == used.length){
            result.add(new ArrayList<>(throughPath));
            return;
        }
        Set<Integer> selectInCurrentLevelSet = new HashSet<>();
        // 依次选择所有可选项
        for(int i=0 ; i < remainNums.length ; i++){
            if(used[i]){
                continue;
            }
            int remainNum = remainNums[i];
            // 同一层使用过
            if(selectInCurrentLevelSet.contains(remainNum)){
                continue;
            }
            // 做选择
            throughPath.add(remainNum);
            used[i] = true;

            permute(throughPath,remainNums,used);
            // 撤销选择
            throughPath.remove(throughPath.size()-1);
            used[i] = false;

            selectInCurrentLevelSet.add(remainNum);
        }
    }
}
