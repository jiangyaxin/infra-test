package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.*;

/**
 *
 * @author jiangyaxin
 * @since 2022/4/12 20:38
 */
public class L39_40_216CombinationSum {

    List<List<Integer>> result = new ArrayList<>();

    /**
     * 力扣 39. 组合总和
     * 给你一个 无重复元素 的整数数组candidates 和一个目标整数target，找出candidates中可以使数字和为目标数target 的 所有不同组合 ，并以列表形式返回。你可以按 任意顺序 返回这些组合。
     * candidates 中的 同一个 数字可以 无限制重复被选取 。如果至少一个数字的被选数量不同，则两种组合是不同的。
     * 对于给定的输入，保证和为target 的不同组合数少于 150 个。
     *
     * 示例1：
     * 输入：candidates = [2,3,6,7], target = 7
     * 输出：[[2,2,3],[7]]
     * 解释：
     * 2 和 3 可以形成一组候选，2 + 2 + 3 = 7 。注意 2 可以使用多次。
     * 7 也是一个候选， 7 = 7 。
     * 仅有这两种组合。
     *
     * 示例2：
     * 输入: candidates = [2,3,5], target = 8
     * 输出: [[2,2,2,2],[2,3,3],[3,5]]
     *
     * 示例 3：
     * 输入: candidates = [2], target = 1
     * 输出: []
     *
     * 提示：
     * 1 <= candidates.length <= 30
     * 1 <= candidates[i] <= 200
     * candidate 中的每个元素都 互不相同
     * 1 <= target <= 500
     *
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        if(candidates.length == 0){
            return result;
        }
        List<Integer> path = new ArrayList<>();
        Arrays.sort(candidates);
        combinationSum(path,0,candidates,target);
        return result;
    }

    public void combinationSum(List<Integer> throughPath,int start,int[] candidates, int remainTarget) {
        if(remainTarget < 0){
            return;
        }
        if(remainTarget == 0){
            result.add(new ArrayList<>(throughPath));
        }

        for (int i=start ; i < candidates.length ; i++){
            int candidate = candidates[i];
            if(candidate > remainTarget){
                break;
            }
            throughPath.add(candidate);
            combinationSum(throughPath,i,candidates,remainTarget - candidate);
            throughPath.remove(throughPath.size()-1);

        }
    }

    /**
     * 力扣 40. 组合总和 II
     * 给定一个候选人编号的集合 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
     * candidates 中的每个数字在每个组合中只能使用 一次 。
     * 注意：解集不能包含重复的组合。
     *
     * 示例 1:
     * 输入: candidates = [10,1,2,7,6,1,5], target = 8,
     * 输出:
     * [
     * [1,1,6],
     * [1,2,5],
     * [1,7],
     * [2,6]
     * ]
     *
     * 示例 2:
     * 输入: candidates = [2,5,2,1,2], target = 5,
     * 输出:
     * [
     * [1,2,2],
     * [5]
     * ]
     *
     * 提示:
     * 1 <= candidates.length <= 100
     * 1 <= candidates[i] <= 50
     * 1 <= target <= 30
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        if(candidates.length == 0){
            return result;
        }
        List<Integer> path = new ArrayList<>();
        Arrays.sort(candidates);
        combinationSum2(path,0,candidates,target);
        return result;
    }

    public void combinationSum2(List<Integer> throughPath,int start,int[] candidates, int remainTarget) {
        if(remainTarget < 0){
            return;
        }
        if(remainTarget == 0){
            result.add(new ArrayList<>(throughPath));
        }

        Set<Integer> selectInCurrentLevel = new HashSet<>();
        for (int i=start ; i < candidates.length ; i++){
            int candidate = candidates[i];
            if(candidate > remainTarget){
                break;
            }
            if(selectInCurrentLevel.contains(candidate)){
                continue;
            }
            throughPath.add(candidate);
            selectInCurrentLevel.add(candidate);
            combinationSum2(throughPath,i+1,candidates,remainTarget - candidate);
            throughPath.remove(throughPath.size()-1);
        }
    }

    /**
     *
     * 力扣 216. 组合总和 III
     * 找出所有相加之和为 n 的 k 个数的组合，且满足下列条件：
     *
     * 只使用数字1到9
     * 每个数字最多使用一次
     * 返回 所有可能的有效组合的列表 。该列表不能包含相同的组合两次，组合可以以任何顺序返回。
     *
     * 示例 1:
     * 输入: k = 3, n = 7
     * 输出: [[1,2,4]]
     * 解释:
     * 1 + 2 + 4 = 7
     * 没有其他符合的组合了。
     *
     * 示例 2:
     * 输入: k = 3, n = 9
     * 输出: [[1,2,6], [1,3,5], [2,3,4]]
     * 解释:
     * 1 + 2 + 6 = 9
     * 1 + 3 + 5 = 9
     * 2 + 3 + 4 = 9
     * 没有其他符合的组合了。
     *
     * 示例 3:
     * 输入: k = 4, n = 1
     * 输出: []
     * 解释: 不存在有效的组合。
     * 在[1,9]范围内使用4个不同的数字，我们可以得到的最小和是1+2+3+4 = 10，因为10 > 1，没有有效的组合。
     *
     * 提示:
     * 2 <= k <= 9
     * 1 <= n <= 60
     */
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<Integer> path = new ArrayList<>(k);
        combinationSum3(path,1,9,k,n);
        return result;
    }

    public void combinationSum3(List<Integer> throughPath,int start,int end,int k,int remainNum){
        if(throughPath.size() == k && remainNum == 0){
            result.add(new ArrayList<>(throughPath));
            return;
        }
        if(start > remainNum){
            return;
        }
        if(!(throughPath.size() < k && remainNum > 0)){
            return;
        }

        for(int i = start ; i <= end ; i++){
            throughPath.add(i);
            combinationSum3(throughPath,i+1,end,k,remainNum-i);
            throughPath.remove(throughPath.size()-1);
        }
    }
}
