package com.jyx.feature.test.jdk.algorithm.interview;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jiangyaxin
 * @since 2022/7/28 15:07
 */
public class SpecialNumber {

    private static final Set<String> hasSelectSet = new HashSet<>();

    public boolean isSpecialNumber(int n){
        String nStr = String.valueOf(n);
        if(hasSelectSet.contains(nStr)){
            return false;
        }
        hasSelectSet.add(nStr);

        int sum = 0;
        for(int i = 0 ; i < nStr.length() ; i++){
            Integer currentNumber = Integer.valueOf(String.valueOf(nStr.charAt(i)));
            sum += Math.pow(currentNumber,2);
        }

        if(sum == 1){
            return true;
        }
        return isSpecialNumber(sum);
    }
}
