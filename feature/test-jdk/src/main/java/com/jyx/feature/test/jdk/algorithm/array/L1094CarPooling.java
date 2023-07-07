package com.jyx.feature.test.jdk.algorithm.array;

import java.util.Arrays;

/**
 * 力扣 1094. 拼车
 * 车上最初有 capacity 个空座位。车 只能 向一个方向行驶（也就是说，不允许掉头或改变方向）
 *
 * 给定整数 capacity 和一个数组 trips ,  trip[i] = [numPassengersi, fromi, toi] 表示第 i 次旅行有 numPassengersi 乘客，接他们和放他们的位置分别是 fromi 和 toi 。这些位置是从汽车的初始位置向东的公里数。
 *
 * 当且仅当你可以在所有给定的行程中接送所有乘客时，返回 true，否则请返回 false。
 *
 * 示例 1：
 * 输入：trips = [[2,1,5],[3,3,7]], capacity = 4
 * 输出：false
 *
 * 示例 2：
 * 输入：trips = [[2,1,5],[3,3,7]], capacity = 5
 * 输出：true
 *
 * 提示：
 * 1 <= trips.length <= 1000
 * trips[i].length == 3
 * 1 <= numPassengersi <= 100
 * 0 <= fromi < toi <= 1000
 * 1 <= capacity <= 105
 *
 * @author jiangyaxin
 * @since 2022/5/8 23:25
 */
public class L1094CarPooling {

    public boolean carPooling(int[][] trips, int capacity) {
        DifferenceArray differenceArray = new DifferenceArray(1001);
        for (int[] trip : trips) {
            // from 上车 to 下车 已经没人了 ，所以计算到 to - 1
            differenceArray.increment(trip[1], trip[2] - 1, trip[0]);
        }
        int[] result = differenceArray.result();
        int max = Arrays.stream(result).max().getAsInt();
        return max <= capacity;
    }

}
