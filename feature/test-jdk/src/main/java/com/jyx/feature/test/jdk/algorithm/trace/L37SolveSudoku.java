package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 力扣 37. 解数独
 * 编写一个程序，通过填充空格来解决数独问题。
 *
 * 数独的解法需 遵循如下规则：
 *
 * 数字 1-9 在每一行只能出现一次。
 * 数字 1-9 在每一列只能出现一次。
 * 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。（请参考示例图）
 * 数独部分空格内已填入了数字，空白格用 '.' 表示。
 *
 * 提示：
 * board.length == 9
 * board[i].length == 9
 * board[i][j] 是一位数字或者 '.'
 * 题目数据 保证 输入数独仅有一个解
 *
 * @author jiangyaxin
 * @since 2022/4/14 20:53
 */
public class L37SolveSudoku {

    private static final char SPACE = '.';

    private static final int BOARD_LENGTH = 9;

    /**
     * 行 已使用数字
     */
    private final List<Set<Character>> xUsedCharList = new ArrayList<>();
    /**
     * 列 已使用数字
     */
    private final List<Set<Character>> yUsedCharList = new ArrayList<>();
    /**
     * 九宫格 已使用数字
     */
    private final List<Set<Character>> zUsedCharList = new ArrayList<>();

    private boolean over = false;


    public void solveSudoku(char[][] board) {
        init(board);
        char[][] result = new char[BOARD_LENGTH][BOARD_LENGTH];
        solveSudoku(result,board,0,-1);

        for(int x=0; x < BOARD_LENGTH; x++){
            System.arraycopy(result[x], 0, board[x], 0, BOARD_LENGTH);
        }
    }

    public void solveSudoku(char[][] result,char[][] board,int x,int y) {
        y++;
        if(y == BOARD_LENGTH){
            y = 0;
            x++;
        }

        char cell = board[x][y];
        if(cell != SPACE){
            result[x][y] = cell;
            if(over(result,x,y)){
                return;
            }
            solveSudoku(result,board,x,y);
            if(over(result,x,y)){
                return;
            }
            result[x][y] = SPACE;
        } else {
            for(int num = 1; num <= BOARD_LENGTH ; num++ ){
                char candidate = String.valueOf(num).charAt(0);
                if(!canFill(x,y,candidate)){
                    continue;
                }
                fill(result,x,y,candidate);
                if(over(result,x,y)){
                    return;
                }

                solveSudoku(result,board,x,y);

                if(over(result,x,y)){
                    return;
                }
                pop(result,x,y,candidate);
            }
        }
    }
    
    private void init(char[][] board){
        for (int i = 0 ; i < BOARD_LENGTH ; i++){
            xUsedCharList.add(new HashSet<>());
            yUsedCharList.add(new HashSet<>());
            zUsedCharList.add(new HashSet<>());
        }
        for(int x=0; x < BOARD_LENGTH; x++){
            for(int y=0;y < BOARD_LENGTH ; y++){
                char cell = board[x][y];
                if( cell != SPACE ){
                    fillUsedCharList(x,y,cell);
                }
            }
        }
    }


    private int calculateZIndex(int x, int y){
        x = x + 1;
        y = y + 1;
        // 坐标前有几排九宫格
        int z1 = x % 3 == 0 && x != 0 ? x / 3 - 1 : x / 3;
        // 位于当前排第几个九宫格
        int z2 = y % 3 == 0 && y != 0 ? y / 3 - 1 : y / 3;

        return z1 * 3 + z2;
    }

    private boolean canFill(int x,int y,char cell){
        return !xUsedCharList.get(x).contains(cell)
                && !yUsedCharList.get(y).contains(cell)
                && !zUsedCharList.get(calculateZIndex(x, y)).contains(cell);
    }

    private void fill(char[][] result,int x,int y,char cell){
        result[x][y] = cell;
        fillUsedCharList(x,y,cell);
    }

    private void pop(char[][] result,int x,int y,char cell){
        result[x][y] = SPACE;
        xUsedCharList.get(x).remove(cell);
        yUsedCharList.get(y).remove(cell);
        zUsedCharList.get(calculateZIndex(x,y)).remove(cell);
    }

    private void fillUsedCharList(int x,int y,char cell){
        xUsedCharList.get(x).add(cell);
        yUsedCharList.get(y).add(cell);
        zUsedCharList.get(calculateZIndex(x,y)).add(cell);
    }

    private boolean over(char[][] result,int x,int y){
        if(over){
            return true;
        }else {
            over = x == BOARD_LENGTH-1 && y == BOARD_LENGTH-1 && result[x][y] != SPACE;
            return over;
        }
    }
}
