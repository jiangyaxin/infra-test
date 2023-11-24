package com.jyx.feature.test.jdk.algorithm;

/**
 * 力扣 16. 部分排序
 *
 * 给定一个整数数组，编写一个函数，找出索引m和n，只要将索引区间[m,n]的元素排好序，整个数组就是有序的。注意：n-m尽量最小，也就是说，找出符合条件的最短序列。函数返回值为[m,n]，若不存在这样的m和n（例如整个数组是有序的），请返回[-1,-1]。
 *
 * 示例：
 *
 * 输入： [1,2,4,7,10,11,7,12,6,7,16,18,19]
 * 输出： [3,9]
 * 提示：
 *
 * 0 <= len(array) <= 1000000
 *
 * 方案:先把两端有序的找出来，再把中间无序部分的最大最小值找出来，往两端扩。
 *
 *
 * @author jiangyaxin
 * @since 2021/1/26 15:09
 */
public class L16SubSort {

    public int[] subSort(int[] array) {
        int[] result = new int[]{-1,-1};
        if(array.length < 2){
            return result;
        }

        int left = 0;
        int right = array.length -1;
        while (left < right){
            if(array[left] <= array[left+1]){
                left++;
            }else {
                break;
            }
        }
        while (left < right){
            if(array[right] >= array[right-1]){
                right--;
            }else {
                break;
            }
        }
        if(left == right){
            return result;
        }

        int notSortPartMax = array[left+1];
        int notSortPartMin = notSortPartMax;
        for(int i= left+1; i<right ; i++){
            notSortPartMax = Math.max(notSortPartMax,array[i]);
            notSortPartMin = Math.min(notSortPartMin,array[i]);
        }

        while (left-1 >=0 && array[left-1] > notSortPartMin){
            notSortPartMax = Math.max(notSortPartMax,array[left]);
            notSortPartMin = Math.min(notSortPartMin,array[left]);
            left--;
        }

        while (right+1 < array.length && array[right+1] < notSortPartMax){
            notSortPartMax = Math.max(notSortPartMax,array[right]);
            if(array[right] < notSortPartMin){
                notSortPartMin = array[right];
                while (left-1 >=0 && array[left-1] > notSortPartMin){
                    notSortPartMax = Math.max(notSortPartMax,array[left]);
                    notSortPartMin = Math.min(notSortPartMin,array[left]);
                    left--;
                }
            }
            right++;
        }

        result[0] = left;
        result[1] = right;
        return result;
    }
}

