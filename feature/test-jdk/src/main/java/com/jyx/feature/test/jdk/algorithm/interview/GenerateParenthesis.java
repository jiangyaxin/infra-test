package com.jyx.feature.test.jdk.algorithm.interview;

import java.util.ArrayList;
import java.util.List;

/**
 * 数字 n代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
 * 示例 1：
 * 输入：n = 3
 * 输出：["((()))","(()())","(())()","()(())","()()()"]
 * 示例 2：
 * 输入：n = 1
 * 输出：["()"]
 *
 * @author jiangyaxin
 * @since 2022/7/27 16:10
 */
public class GenerateParenthesis {

    private static final List<String> RESULT = new ArrayList<>(100);

    private static final String LEFT = "(";
    private static final String RIGHT = ")";
    private static final int FACTOR = 2;

    public List<String> generateParenthesis(int n) {
        generateParenthesis("",n,0,n);
        return RESULT;
    }

    public void generateParenthesis(String path ,int left , int right,int n){
        if( path.length() == n*FACTOR && left == 0 && right == 0){
            RESULT.add(path);
            return;
        }

        if (left > 0) {
            path += LEFT;
            generateParenthesis(path, left - 1, right + 1, n);
            path = path.substring(0,path.length()-1);
        }

        if (right > 0) {
            path += RIGHT;
            generateParenthesis(path, left, right - 1, n);
            path = path.substring(0,path.length()-1);
        }
    }


}
