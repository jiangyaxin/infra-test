package com.jyx.feature.test.jdk.algorithm.trace;

/**
 * 力扣 200. 岛屿数量
 * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 *
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 *
 * 此外，你可以假设该网格的四条边均被水包围。
 *
 * 示例 1：
 * 输入：grid = [
 *   ["1","1","1","1","0"],
 *   ["1","1","0","1","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","0","0","0"]
 * ]
 * 输出：1
 *
 * 示例 2：
 * 输入：grid = [
 *   ["1","1","0","0","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","1","0","0"],
 *   ["0","0","0","1","1"]
 * ]
 * 输出：3
 *
 * 提示：
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 300
 * grid[i][j] 的值为 '0' 或 '1'
 *
 * @author jiangyaxin
 * @since 2022/4/19 19:20
 */
public class L200NumIslands {

    private static final char LAND = '1';

    private int maxX;

    private int maxY;

    public int numIslands(char[][] grid) {
      maxY = grid.length;
      maxX = grid[0].length;
      boolean[][] marked = new boolean[maxY][maxX];
      int result = 0;

      for(int x = 0 ; x < maxX ; x++){
          for(int y = 0 ; y < maxY ; y++){
              if(!marked[y][x] && grid[y][x] == LAND){
                  numIslands(grid,marked,new Position(x,y));
                  result++;
              }
          }
      }
      return result;
    }

    public void numIslands(char[][] grid,boolean[][] marked,Position currentPosition) {
        marked[currentPosition.y][currentPosition.x] = true;

        Position upPosition = currentPosition.getUpPosition();
        if(upPosition != null){
            if(!marked[upPosition.y][upPosition.x] && grid[upPosition.y][upPosition.x] == LAND){
                numIslands(grid,marked,upPosition);
            }
        }

        Position downPosition = currentPosition.getDownPosition();
        if(downPosition != null){
            if(!marked[downPosition.y][downPosition.x] && grid[downPosition.y][downPosition.x] == LAND){
                numIslands(grid,marked,downPosition);
            }
        }

        Position leftPosition = currentPosition.getLeftPosition();
        if(leftPosition != null){
            if(!marked[leftPosition.y][leftPosition.x] && grid[leftPosition.y][leftPosition.x] == LAND){
                numIslands(grid,marked,leftPosition);
            }
        }

        Position rightPosition = currentPosition.getRightPosition();
        if(rightPosition != null){
            if(!marked[rightPosition.y][rightPosition.x] && grid[rightPosition.y][rightPosition.x] == LAND){
                numIslands(grid,marked,rightPosition);
            }
        }
    }


    class Position{
        int x;
        int y;

        Position(int x,int y){
            this.x = x;
            this.y = y;
        }

        public Position getUpPosition(){
            if(y-1 < 0){
                return null;
            }
            return new Position(x,y-1);
        }

        public Position getDownPosition(){
            if(y+1 == maxY){
                return null;
            }
            return new Position(x,y+1);
        }

        public Position getLeftPosition(){
            if(x-1 < 0){
                return null;
            }
            return new Position(x-1,y);
        }

        public Position getRightPosition(){
            if(x+1 == maxX){
                return null;
            }
            return new Position(x+1,y);
        }

    }
}
