package com.jyx.feature.test.jdk.algorithm.trace;

/**
 * 力扣 1905. 统计子岛屿
 * 给你两个 m x n 的二进制矩阵 grid1 和 grid2 ，它们只包含 0 （表示水域）和 1 （表示陆地）。一个 岛屿 是由 四个方向 （水平或者竖直）上相邻的 1 组成的区域。任何矩阵以外的区域都视为水域。
 *
 * 如果 grid2 的一个岛屿，被 grid1 的一个岛屿 完全 包含，也就是说 grid2 中该岛屿的每一个格子都被 grid1 中同一个岛屿完全包含，那么我们称 grid2 中的这个岛屿为 子岛屿 。
 *
 * 请你返回 grid2 中 子岛屿 的 数目 。
 *
 * 示例 1：
 * 输入：grid1 = [[1,1,1,0,0],[0,1,1,1,1],[0,0,0,0,0],[1,0,0,0,0],[1,1,0,1,1]], grid2 = [[1,1,1,0,0],[0,0,1,1,1],[0,1,0,0,0],[1,0,1,1,0],[0,1,0,1,0]]
 * 输出：3
 * 解释：如上图所示，左边为 grid1 ，右边为 grid2 。
 * grid2 中标红的 1 区域是子岛屿，总共有 3 个子岛屿。
 *
 * 示例 2：
 * 输入：grid1 = [[1,0,1,0,1],[1,1,1,1,1],[0,0,0,0,0],[1,1,1,1,1],[1,0,1,0,1]], grid2 = [[0,0,0,0,0],[1,1,1,1,1],[0,1,0,1,0],[0,1,0,1,0],[1,0,0,0,1]]
 * 输出：2
 * 解释：如上图所示，左边为 grid1 ，右边为 grid2 。
 * grid2 中标红的 1 区域是子岛屿，总共有 2 个子岛屿。
 *
 *
 * 提示：
 * m == grid1.length == grid2.length
 * n == grid1[i].length == grid2[i].length
 * 1 <= m, n <= 500
 * grid1[i][j] 和 grid2[i][j] 都要么是 0 要么是 1 。
 *
 * @author jiangyaxin
 * @since 2022/4/19 21:41
 */
public class L1905CountSubIslands {

    private static final int LAND = 1;
    private static final int WATER = 0;

    private int maxX;
    private int maxY;


    public int countSubIslands(int[][] grid1, int[][] grid2) {
        maxY = grid2.length;
        maxX = grid2[0].length;

        int result = 0;
        for (int x = 0 ; x < maxX ; x++){
            for(int y = 0 ; y < maxY ; y++){
                if(grid2[y][x] == LAND){
                    if(countSubIslands(grid1,grid2,x,y)){
                        result++;
                    }
                }
            }
        }
        return result;
    }

    public boolean countSubIslands(int[][] grid1, int[][] grid2,int x, int y) {
        if(x < 0 || x == maxX || y < 0 || y == maxY){
            return true;
        }

        if(grid2[y][x] == WATER){
            return true;
        }
        boolean isSubIsland = true;
        grid2[y][x] = WATER;
        if(grid1[y][x] == WATER){
            isSubIsland = false;
        }
        isSubIsland = isSubIsland
                & countSubIslands(grid1, grid2, x-1, y)
                & countSubIslands(grid1, grid2, x+1, y)
                & countSubIslands(grid1, grid2, x, y-1)
                & countSubIslands(grid1, grid2, x, y+1);
        return isSubIsland;
    }
}
