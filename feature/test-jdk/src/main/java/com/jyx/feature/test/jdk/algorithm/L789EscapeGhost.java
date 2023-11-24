package com.jyx.feature.test.jdk.algorithm;

/**
 * 力扣 789. 逃脱阻碍者
 *
 * 你在进行一个简化版的吃豆人游戏。你从 [0, 0] 点开始出发，你的目的地是target = [xtarget, ytarget] 。地图上有一些阻碍者，以数组 ghosts 给出，第 i 个阻碍者从ghosts[i] = [xi, yi]出发。所有输入均为 整数坐标 。
 *
 * 每一回合，你和阻碍者们可以同时向东，西，南，北四个方向移动，每次可以移动到距离原位置 1 个单位 的新位置。当然，也可以选择 不动 。所有动作 同时 发生。
 *
 * 如果你可以在任何阻碍者抓住你 之前 到达目的地（阻碍者可以采取任意行动方式），则被视为逃脱成功。如果你和阻碍者同时到达了一个位置（包括目的地）都不算是逃脱成功。
 *
 * 只有在你有可能成功逃脱时，输出 true ；否则，输出 false 。
 *
 * 示例 1：
 *
 * 输入：ghosts = [[1,0],[0,3]], target = [0,1]
 * 输出：true
 * 解释：你可以直接一步到达目的地 (0,1) ，在 (1, 0) 或者 (0, 3) 位置的阻碍者都不可能抓住你。
 * 示例 2：
 *
 * 输入：ghosts = [[1,0]], target = [2,0]
 * 输出：false
 * 解释：你需要走到位于 (2, 0) 的目的地，但是在 (1, 0) 的阻碍者位于你和目的地之间。
 * 示例 3：
 *
 * 输入：ghosts = [[2,0]], target = [1,0]
 * 输出：false
 * 解释：阻碍者可以和你同时达到目的地。
 * 示例 4：
 *
 * 输入：ghosts = [[5,0],[-10,-2],[0,-5],[-2,-2],[-7,1]], target = [7,7]
 * 输出：false
 * 示例 5：
 *
 * 输入：ghosts = [[-1,0],[0,1],[-1,0],[0,1],[-1,0]], target = [0,0]
 * 输出：true
 *
 * 提示：
 *
 * 1 <= ghosts.length <= 100
 * ghosts[i].length == 2
 * -104 <= xi, yi <= 104
 * 同一位置可能有 多个阻碍者 。
 * target.length == 2
 * -104 <= xtarget, ytarget <= 104
 *
 * 方案分析：
 *          C
 *         /  \
 *        /    \
 *       /      \
 *      /        \
 *     /   \D     \
 *    /      \     \
 *   /         \    \
 *  /            \   \
 * A               \
 *                     B
 * A表示起点 B表示鬼魂的位置 目的地为C
 * 如果鬼魂要在中间拦截 AC上必须有一点D
 * 使得AD = DB 通过三角不等式 AC = AD + DC = DB + DC >= BC
 * 如果鬼魂可以拦截到 那么鬼魂最好的做法就是在终点等着
 * 而不是去中间拦截
 *
 * @author jiangyaxin
 * @since 2021/1/27 16:46
 */
public class L789EscapeGhost {

    public boolean escapeGhosts(int[][] ghosts, int[] target) {
        for (int[] ghost : ghosts) {
            if (getManhattanDistance(ghost, target) <= getManhattanDistance(new int[]{0, 0}, target)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private int getManhattanDistance(int[] location,int[] target){
        return Math.abs(target[0] - location[0]) + Math.abs(target[1] - location[1]);
    }
}
