package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.ArrayList;
import java.util.List;

/**
 * 力扣 131. 分割回文串
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是 回文串 。返回 s 所有可能的分割方案。
 * 回文串 是正着读和反着读都一样的字符串。
 *
 * 示例 1：
 * 输入：s = "aab"
 * 输出：[["a","a","b"],["aa","b"]]
 *
 * 示例 2：
 * 输入：s = "a"
 * 输出：[["a"]]
 *
 * 提示：
 * 1 <= s.length <= 16
 * s 仅由小写英文字母组成
 *
 * @author jiangyaxin
 * @since 2022/4/12 22:00
 */
public class L131Partition {

    List<List<String>> result = new ArrayList<>();

    public List<List<String>> partition(String s) {
        List<String> path = new ArrayList<>();
        partition(path,s);
        return result;
    }

    public void partition(List<String> throughPath,String remainS) {
        if(remainS.length() == 1){
            throughPath.add(remainS);
            result.add(new ArrayList<>(throughPath));
            throughPath.remove(throughPath.size()-1);
            return;
        }
        for(int splitCharAt = 1; splitCharAt < remainS.length() ; splitCharAt++){
            String pathNode = remainS.substring(0, splitCharAt);
            if(!isPalindrome(pathNode)){
                continue;
            }
            String nextRemainS = remainS.substring(splitCharAt);
            throughPath.add(pathNode);
            partition(throughPath,nextRemainS);
            throughPath.remove(throughPath.size()-1);
        }

        if(isPalindrome(remainS)){
            throughPath.add(remainS);
            result.add(new ArrayList<>(throughPath));
            throughPath.remove(throughPath.size()-1);
        }
    }

    private boolean isPalindrome(String str){
        for(int charAt = 0 ; charAt < str.length() ; charAt++){
            if(str.charAt(charAt) != str.charAt(str.length()-1-charAt)){
                return false;
            }
        }
        return true;
    }
}
