package com.jyx.feature.test.jdk.algorithm.bfs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

/**
 * 力扣 773. 滑动谜题
 * 在一个 2 x 3 的板上（board）有 5 块砖瓦，用数字 1~5 来表示, 以及一块空缺用 0 来表示。一次 移动 定义为选择 0 与一个相邻的数字（上下左右）进行交换.
 *
 * 最终当板 board 的结果是 [[1,2,3],[4,5,0]] 谜板被解开。
 *
 * 给出一个谜板的初始状态 board ，返回最少可以通过多少次移动解开谜板，如果不能解开谜板，则返回 -1 。
 *
 * 示例 1：
 * 输入：board = [[1,2,3],[4,0,5]]
 * 输出：1
 * 解释：交换 0 和 5 ，1 步完成
 *
 * 示例 2:
 * 输入：board = [[1,2,3],[5,4,0]]
 * 输出：-1
 * 解释：没有办法完成谜板
 *
 * 示例 3:
 * 输入：board = [[4,1,2],[5,0,3]]
 * 输出：5
 * 解释：
 * 最少完成谜板的最少移动次数是 5 ，
 * 一种移动路径:
 * 尚未移动: [[4,1,2],[5,0,3]]
 * 移动 1 次: [[4,1,2],[0,5,3]]
 * 移动 2 次: [[0,1,2],[4,5,3]]
 * 移动 3 次: [[1,0,2],[4,5,3]]
 * 移动 4 次: [[1,2,0],[4,5,3]]
 * 移动 5 次: [[1,2,3],[4,5,0]]
 *
 * 提示：
 * board.length == 2
 * board[i].length == 3
 * 0 <= board[i][j] <= 5
 * board[i][j] 中每个值都 不同
 *
 * @author jiangyaxin
 * @since 2022/4/21 19:09
 */
public class L773SlidingPuzzle {

    private int maxX;

    private int maxLength;

    private static final char SPACE_TEMP = '.';
    private static final char SPACE = '0';
    private static final String TARGET = "123450";

    public int slidingPuzzle(int[][] board) {
        int maxY = board.length;
        maxX = board[0].length;
        maxLength =  maxX * maxY;
        Queue<String> queue = new ArrayBlockingQueue<>(1000);
        Set<String> visited = new HashSet<>();
        StringBuilder startBuilder = new StringBuilder();
        for (int[] ints : board) {
            startBuilder.append(Arrays.stream(ints).boxed().map(String::valueOf).collect(Collectors.joining()));
        }
        String start = startBuilder.toString();
        queue.offer(start);
        int result = 0;
        while (!queue.isEmpty()){
            int size = queue.size();
            for(int i = 0; i < size ; i++){
                String poll = queue.poll();
                if(visited.contains(poll)){
                    continue;
                }
                if(poll.equals(TARGET)){
                    return result;
                }
                visited.add(poll);

                String up = up(poll, poll.indexOf(SPACE));
                if(up != null){
                    queue.offer(up);
                }

                String down = down(poll, poll.indexOf(SPACE));
                if(down != null){
                    queue.offer(down);
                }

                String left = left(poll, poll.indexOf(SPACE));
                if(left != null){
                    queue.offer(left);
                }

                String right = right(poll, poll.indexOf(SPACE));
                if(right != null){
                    queue.offer(right);
                }

            }
            result++;
        }

        return -1;
    }

    private String up(String boardStr,int chatAt0){
        int replaceCharAt = chatAt0 - 3;
        if(replaceCharAt >= 0){
            char replaceChar = boardStr.charAt(replaceCharAt);
            boardStr = boardStr.replace(replaceChar,SPACE_TEMP);
            boardStr = boardStr.replace(SPACE,replaceChar);
            boardStr = boardStr.replace(SPACE_TEMP,SPACE);
            return boardStr;
        } else {
            return null;
        }
    }

    private String down(String boardStr,int chatAt0){
        int replaceCharAt = chatAt0 + 3;
        if(replaceCharAt < maxLength){
            char replaceChar = boardStr.charAt(replaceCharAt);
            boardStr = boardStr.replace(replaceChar,SPACE_TEMP);
            boardStr = boardStr.replace(SPACE,replaceChar);
            boardStr = boardStr.replace(SPACE_TEMP,SPACE);
            return boardStr;
        } else {
            return null;
        }
    }

    private String left(String boardStr,int chatAt0){
        int replaceCharAt = chatAt0 - 1;
        if(replaceCharAt < 0){
            return null;
        }
        if(replaceCharAt % maxX != maxX-1){
            char replaceChar = boardStr.charAt(replaceCharAt);
            boardStr = boardStr.replace(replaceChar,SPACE_TEMP);
            boardStr = boardStr.replace(SPACE,replaceChar);
            boardStr = boardStr.replace(SPACE_TEMP,SPACE);
            return boardStr;
        } else {
            return null;
        }
    }

    private String right(String boardStr,int chatAt0){
        int replaceCharAt = chatAt0 + 1;
        if(replaceCharAt == maxLength){
            return null;
        }
        if(replaceCharAt % maxX != 0){
            char replaceChar = boardStr.charAt(replaceCharAt);
            boardStr = boardStr.replace(replaceChar,SPACE_TEMP);
            boardStr = boardStr.replace(SPACE,replaceChar);
            boardStr = boardStr.replace(SPACE_TEMP,SPACE);
            return boardStr;
        } else {
            return null;
        }
    }


}
