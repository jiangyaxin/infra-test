package com.jyx.feature.test.jdk.algorithm.trace;

/**
 * 力扣 1254. 统计封闭岛屿的数目
 * 二维矩阵 grid 由 0 （土地）和 1 （水）组成。岛是由最大的4个方向连通的 0 组成的群，封闭岛是一个 完全 由1包围（左、上、右、下）的岛。
 *
 * 请返回 封闭岛屿 的数目。
 *
 * 示例 1：
 * 输入：grid = [[1,1,1,1,1,1,1,0],[1,0,0,0,0,1,1,0],[1,0,1,0,1,1,1,0],[1,0,0,0,0,1,0,1],[1,1,1,1,1,1,1,0]]
 * 输出：2
 * 解释：
 * 灰色区域的岛屿是封闭岛屿，因为这座岛屿完全被水域包围（即被 1 区域包围）。
 *
 * 示例 2：
 * 输入：grid = [[0,0,1,0,0],[0,1,0,1,0],[0,1,1,1,0]]
 * 输出：1
 *
 * 示例 3：
 * 输入：grid = [[1,1,1,1,1,1,1],
 *              [1,0,0,0,0,0,1],
 *              [1,0,1,1,1,0,1],
 *              [1,0,1,0,1,0,1],
 *              [1,0,1,1,1,0,1],
 *              [1,0,0,0,0,0,1],
 *              [1,1,1,1,1,1,1]]
 * 输出：2
 *
 * 提示：
 * 1 <= grid.length, grid[0].length <= 100
 * 0 <= grid[i][j] <=1
 *
 * @author jiangyaxin
 * @since 2022/4/19 20:06
 */
public class L1254ClosedIsland {

    private static final int LAND = 0;

    private int maxX;
    private int maxY;

    public int closedIsland(int[][] grid) {
        maxY = grid.length;
        maxX = grid[0].length;
        boolean[][] used = new boolean[grid.length][grid[0].length];
        int result = 0;
        for(int x = 0 ; x< maxX ; x++){
            for(int y = 0 ; y< maxY ; y++){
                if(!used[y][x] && grid[y][x] == LAND){
                    if(closedIsland(grid,used,x,y)){
                        result++;
                    }
                }
            }
        }
        return result;
    }

    public boolean closedIsland(int[][] grid,boolean[][] used,int x,int y){
        used[y][x] = true;
        boolean closedIsland = x != 0 && x != maxX - 1 && y != 0 && y != maxY - 1;

        if(y - 1 >=0 ) {
            if (grid[y - 1][x] == LAND && !used[y - 1][x]) {
                if (!closedIsland(grid, used, x, y - 1)) {
                    closedIsland = false ;
                }
            }
        }
        if(y + 1 < maxY ) {
            if (grid[y + 1][x] == LAND && !used[y + 1][x]) {
                if (!closedIsland(grid, used, x, y + 1)) {
                    closedIsland = false ;
                }
            }
        }

        if(x - 1 >=0 ) {
            if (grid[y][x - 1] == LAND && !used[y][x - 1]) {
                if (!closedIsland(grid, used, x - 1, y)) {
                    closedIsland = false ;
                }
            }
        }

        if(x + 1 < maxX ) {
            if (grid[y][x + 1] == LAND && !used[y][x + 1]) {
                if (!closedIsland(grid, used, x + 1, y)) {
                    closedIsland = false ;
                }
            }
        }
        return closedIsland;
    }

}
