package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.*;

/**
 * 力扣 17. 电话号码的字母组合
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
 *
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 *
 * 示例 1：
 * 输入：digits = "23"
 * 输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
 *
 * 示例 2：
 * 输入：digits = ""
 * 输出：[]
 *
 * 示例 3：
 * 输入：digits = "2"
 * 输出：["a","b","c"]
 *
 * 提示：
 * 0 <= digits.length <= 4
 * digits[i] 是范围 ['2', '9'] 的一个数字。
 *
 * @author jiangyaxin
 * @since 2022/4/12 20:56
 */
public class L17LetterCombination {

    private static final Map<String,String> NUMBER_LETTER_MAP = new HashMap<>();

    static {
        NUMBER_LETTER_MAP.put("2","abc");
        NUMBER_LETTER_MAP.put("3","def");
        NUMBER_LETTER_MAP.put("4","ghi");
        NUMBER_LETTER_MAP.put("5","jkl");
        NUMBER_LETTER_MAP.put("6","mno");
        NUMBER_LETTER_MAP.put("7","pqrs");
        NUMBER_LETTER_MAP.put("8","tuv");
        NUMBER_LETTER_MAP.put("9","wxyz");
    }

    List<String> result = new ArrayList<>();

    public List<String> letterCombinations(String digits) {
        if(Objects.equals(digits.trim(),"")){
            return result;
        }
        StringBuilder path = new StringBuilder();
        letterCombinations(path,digits,0);
        return result;
    }

    public void letterCombinations(StringBuilder path,String digits,int digitAt) {
        if(digitAt == digits.length()){
            result.add(path.toString());
            return;
        }

        char number = digits.charAt(digitAt);
        String letter = NUMBER_LETTER_MAP.get(String.valueOf(number));
        for(int letterAt = 0 ; letterAt < letter.length() ; letterAt++){
            path.append(letter.charAt(letterAt));
            letterCombinations(path,digits,digitAt+1);
            path.deleteCharAt(path.length()-1);
        }

    }
}
