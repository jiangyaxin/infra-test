package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.ArrayList;
import java.util.List;

/**
 * 力扣 22. 括号生成
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
 *
 * 示例 1：
 * 输入：n = 3
 * 输出：["((()))","(()())","(())()","()(())","()()()"]
 *
 * 示例 2：
 * 输入：n = 1
 * 输出：["()"]
 *
 *
 * 提示：
 * 1 <= n <= 8
 *
 * @author jiangyaxin
 * @since 2022/4/20 18:44
 */
public class L22GenerateParenthesis {

    private static final  String LEFT = "(";
    private static final  String RIGHT = ")";

    List<String> result = new ArrayList<>();

    public List<String> generateParenthesis(int n) {
        List<String> path = new ArrayList<>();
        generateParenthesis(path,n,0,n);
        return result;
    }

    public void generateParenthesis(List<String> throughPath,int left,int right,int n) {
        if(throughPath.size() == n+n){
            result.add(String.join("",throughPath));
            return;
        }
        if(left > 0){
            throughPath.add(LEFT);
            generateParenthesis(throughPath,left-1,right+1,n);
            throughPath.remove(throughPath.size()-1);
        }
        if(right > 0){
            throughPath.add(RIGHT);
            generateParenthesis(throughPath,left,right-1,n);
            throughPath.remove(throughPath.size()-1);
        }

    }
}
