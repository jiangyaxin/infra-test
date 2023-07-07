package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.ArrayList;
import java.util.List;

/**
 * 力扣 46. 全排列
 * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
 *
 * 示例 1：
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 *
 * 提示：
 * 1 <= nums.length <= 6
 * -10 <= nums[i] <= 10
 * nums 中的所有整数 互不相同
 *
 * @author jiangyaxin
 * @since 2022/4/11 20:57
 */
public class L46FullPermute {

    List<List<Integer>> result = new ArrayList<>();

    public List<List<Integer>> permute(int[] nums) {
        List<Integer> path = new ArrayList<>(nums.length);
        permute(path,nums,nums.length);
        return result;
    }

    private void permute(List<Integer> throughPath,int[] remainNums,int numLength){
        // 临界值,最后一个节点
        if(throughPath.size() == numLength){
            result.add(new ArrayList<>(throughPath));
            return;
        }
        // 依次选择所有可选项
        for(int num : remainNums){
            if(throughPath.contains(num)){
                continue;
            }
            // 做选择
            throughPath.add(num);
            permute(throughPath,remainNums,numLength);
            // 撤销选择
            throughPath.remove(throughPath.size()-1);
        }
    }
}
