package com.jyx.feature.test.jdk.algorithm.array;

/**
 * 差分数组
 *
 * @author jiangyaxin
 * @since 2022/5/5 22:17
 */
public class DifferenceArray {

    private int[] differenceArray;

    public DifferenceArray(int capacity){
        differenceArray = new int[capacity];
    }

    public void increment(int start,int end,int value){
        differenceArray[start] += value;
        if(end+1 < differenceArray.length) {
            differenceArray[end + 1] -= value;
        }
    }

    public int[] result(){
        int[] result = new int[differenceArray.length];
        result[0] = differenceArray[0];
        for(int i = 1 ; i < result.length ; i++){
            result[i] = result[i-1] + differenceArray[i];
        }
        return result;
    }
}
