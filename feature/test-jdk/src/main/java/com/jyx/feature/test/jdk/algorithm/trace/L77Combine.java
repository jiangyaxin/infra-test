package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.ArrayList;
import java.util.List;

/**
 * 力扣 77. 组合
 * 给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合。
 *
 * 你可以按 任何顺序 返回答案。
 *
 * 示例 1：
 * 输入：n = 4, k = 2
 * 输出：
 * [
 *   [2,4],
 *   [3,4],
 *   [2,3],
 *   [1,2],
 *   [1,3],
 *   [1,4],
 * ]
 *
 * 示例 2：
 * 输入：n = 1, k = 1
 * 输出：[[1]]
 *
 * 提示：
 * 1 <= n <= 20
 * 1 <= k <= n
 *
 * @author jiangyaxin
 * @since 2022/4/12 20:14
 */
public class L77Combine {

    List<List<Integer>> result = new ArrayList<>();

    public List<List<Integer>> combine(int n, int k) {
        List<Integer> path = new ArrayList<>(k);
        combine(path,1,n,k);
        return result;
    }

    public void combine(List<Integer> throughPath,int start,int end,int k){
        if(throughPath.size() == k){
            result.add(new ArrayList<>(throughPath));
            return;
        }

        for(int i = start ; i <= end ; i++){
            throughPath.add(i);
            combine(throughPath,i+1,end,k);
            throughPath.remove(throughPath.size()-1);
        }
    }
}
