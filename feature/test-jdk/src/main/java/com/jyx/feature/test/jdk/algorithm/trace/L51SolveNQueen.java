package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

/**
 * 力扣 51. N 皇后
 * n 皇后问题 研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 *
 * 给你一个整数 n ，返回所有不同的 n 皇后问题 的解决方案。
 *
 * 每一种解法包含一个不同的 n 皇后问题 的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。
 *
 * 示例 1：
 * 输入：n = 4
 * 输出：[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
 * 解释：如上图所示，4 皇后问题存在两个不同的解法。
 *
 * 示例 2：
 * 输入：n = 1
 * 输出：[["Q"]]
 *
 * 提示：
 * 1 <= n <= 9
 *
 * @author jiangyaxin
 * @since 2022/4/13 21:31
 */
public class L51SolveNQueen {

    private final static String QUEEN = "Q";

    private final static String SPACE = ".";

    List<List<String>> result = new ArrayList<>();

    public List<List<String>> solveNQueens(int n) {
        List<Position> path = new ArrayList<>();
        solveNQueens(path,0,n);
        return result;
    }

    public void solveNQueens(List<Position> throughPath, int y, int n) {
        if(!(throughPath.size() <= n && y <= n)){
            return;
        }
        if(throughPath.size() == n && y == n){
            result.add(transfer(throughPath,n));
            return;
        }
        for(int x=0; x<n; x++){
            if(!canSet(throughPath,y,x)){
                continue;
            }
            throughPath.add(new Position(x, y));
            solveNQueens(throughPath,y+1,n);
            throughPath.remove(throughPath.size()-1);
        }
    }

    private List<String> transfer(List<Position> throughPath,int n){
        return throughPath.stream()
                .sorted(Comparator.comparingInt(position -> position.y))
                .map(pathNode -> IntStream.range(0,n).mapToObj(x -> x == pathNode.x ? QUEEN : SPACE).collect(Collectors.joining()))
                .collect(Collectors.toList());
    }

    private boolean canSet(List<Position> throughPath,int y,int x){
        if(throughPath.size() == 0){
            return true;
        }
        return throughPath.stream()
                .allMatch(position -> {
                    if (position.x == x || position.y == y ) {
                        return false;
                    }
                    return abs(position.x - x) != abs(position.y - y);
                });
    }

    static class Position{
        public int x;
        public int y;

        Position(int x,int y){
            this.x = x;
            this.y = y;
        }
    }

}
