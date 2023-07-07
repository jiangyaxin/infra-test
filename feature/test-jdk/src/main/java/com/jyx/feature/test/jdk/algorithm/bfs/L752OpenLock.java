package com.jyx.feature.test.jdk.algorithm.bfs;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 力扣 752. 打开转盘锁
 * 你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' 。每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9' 。每次旋转都只能旋转一个拨轮的一位数字。
 *
 * 锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。
 *
 * 列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。
 *
 * 字符串 target 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 -1 。
 *
 * 示例 1:
 * 输入：deadends = ["0201","0101","0102","1212","2002"], target = "0202"
 * 输出：6
 * 解释：
 * 可能的移动序列为 "0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202"。
 * 注意 "0000" -> "0001" -> "0002" -> "0102" -> "0202" 这样的序列是不能解锁的，
 * 因为当拨动到 "0102" 时这个锁就会被锁定。
 *
 * 示例 2:
 * 输入: deadends = ["8888"], target = "0009"
 * 输出：1
 * 解释：把最后一位反向旋转一次即可 "0000" -> "0009"。
 *
 * 示例 3:
 * 输入: deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
 * 输出：-1
 * 解释：无法旋转到目标数字且不被锁定。
 *
 * 提示：
 * 1 <= deadends.length <= 500
 * deadends[i].length == 4
 * target.length == 4
 * target 不在 deadends 之中
 * target 和 deadends[i] 仅由若干位数字组成
 *
 * @author jiangyaxin
 * @since 2022/4/20 21:00
 */
public class L752OpenLock {

    private static final List<String> NUM_LIST = IntStream.range(0,10).mapToObj(String::valueOf).collect(Collectors.toList());

    private static final String START = "0000";

    public int openLock(String[] deadends, String target) {
        Set<String> visited = new HashSet<>();
        List<String> deadEndList = Arrays.stream(deadends).collect(Collectors.toList());
        Queue<String> queue = new ArrayBlockingQueue<>(10000);
        if(deadEndList.contains(START)){
            return -1;
        }
        queue.offer(START);
        visited.add(START);
        int result = 0;

        while (!queue.isEmpty()){
            int size = queue.size();
            for(int i = 0 ; i < size ; i++){
                String lock = queue.poll();
                if(lock.equals(target)){
                    return result;
                }

                int first = NUM_LIST.indexOf(String.valueOf(lock.charAt(0)));
                String newLockPre = NUM_LIST.get((first - 1 + 10) % 10)+lock.substring(1);
                String newLockPost = NUM_LIST.get((first + 1 + 10) % 10)+lock.substring(1);
                if(!visited.contains(newLockPre) && !deadEndList.contains(newLockPre)){
                    queue.offer(newLockPre);
                    visited.add(newLockPre);
                }
                if(!visited.contains(newLockPost) && !deadEndList.contains(newLockPost)){
                    queue.offer(newLockPost);
                    visited.add(newLockPost);
                }

                int second = NUM_LIST.indexOf(String.valueOf(lock.charAt(1)));
                newLockPre = lock.substring(0,1) + NUM_LIST.get((second - 1 + 10) % 10) + lock.substring(2);
                newLockPost = lock.substring(0,1) + NUM_LIST.get((second + 1 + 10) % 10) + lock.substring(2);
                if(!visited.contains(newLockPre) && !deadEndList.contains(newLockPre)){
                    queue.offer(newLockPre);
                    visited.add(newLockPre);
                }
                if(!visited.contains(newLockPost) && !deadEndList.contains(newLockPost)){
                    queue.offer(newLockPost);
                    visited.add(newLockPost);
                }

                int third = NUM_LIST.indexOf(String.valueOf(lock.charAt(2)));
                newLockPre = lock.substring(0,2) + NUM_LIST.get((third - 1 + 10) % 10) + lock.substring(3);
                newLockPost = lock.substring(0,2) + NUM_LIST.get((third + 1 + 10) % 10) + lock.substring(3);
                if(!visited.contains(newLockPre) && !deadEndList.contains(newLockPre)){
                    queue.offer(newLockPre);
                    visited.add(newLockPre);
                }
                if(!visited.contains(newLockPost) && !deadEndList.contains(newLockPost)){
                    queue.offer(newLockPost);
                    visited.add(newLockPost);
                }

                int four = NUM_LIST.indexOf(String.valueOf(lock.charAt(3)));
                newLockPre = lock.substring(0,3) + NUM_LIST.get((four - 1 + 10) % 10);
                newLockPost = lock.substring(0,3) + NUM_LIST.get((four + 1 + 10) % 10);
                if(!visited.contains(newLockPre) && !deadEndList.contains(newLockPre)){
                    queue.offer(newLockPre);
                    visited.add(newLockPre);
                }
                if(!visited.contains(newLockPost) && !deadEndList.contains(newLockPost)){
                    queue.offer(newLockPost);
                    visited.add(newLockPost);
                }
            }
            result ++;
        }
        return -1;
    }

    public int doubleBfs(String[] deadends, String target) {
        List<String> deadEndList = Arrays.stream(deadends).collect(Collectors.toList());
        if(deadEndList.contains(START)){
            return -1;
        }

        Set<String> startSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        Set<String> visited = new HashSet<>();

        startSet.add(START);
        endSet.add(target);
        int result = 0;

        while (startSet.size() > 0 && endSet.size() > 0){
            Set<String> exchange;
            if (startSet.size() > endSet.size()) {
                exchange = startSet;
                startSet = endSet;
                endSet = exchange;
            }
            Set<String> temp = new HashSet<>();

            for(String lock : startSet ){
                visited.add(lock);
                if(endSet.contains(lock)){
                    return result;
                }

                int first = NUM_LIST.indexOf(String.valueOf(lock.charAt(0)));
                String newLockPre = NUM_LIST.get((first - 1 + 10) % 10)+lock.substring(1);
                String newLockPost = NUM_LIST.get((first + 1 + 10) % 10)+lock.substring(1);
                if(!visited.contains(newLockPre) && !deadEndList.contains(newLockPre)){
                    temp.add(newLockPre);
                }
                if(!visited.contains(newLockPost) && !deadEndList.contains(newLockPost)){
                    temp.add(newLockPost);
                }

                int second = NUM_LIST.indexOf(String.valueOf(lock.charAt(1)));
                newLockPre = lock.substring(0,1) + NUM_LIST.get((second - 1 + 10) % 10) + lock.substring(2);
                newLockPost = lock.substring(0,1) + NUM_LIST.get((second + 1 + 10) % 10) + lock.substring(2);
                if(!visited.contains(newLockPre) && !deadEndList.contains(newLockPre)){
                    temp.add(newLockPre);
                }
                if(!visited.contains(newLockPost) && !deadEndList.contains(newLockPost)){
                    temp.add(newLockPost);
                }

                int third = NUM_LIST.indexOf(String.valueOf(lock.charAt(2)));
                newLockPre = lock.substring(0,2) + NUM_LIST.get((third - 1 + 10) % 10) + lock.substring(3);
                newLockPost = lock.substring(0,2) + NUM_LIST.get((third + 1 + 10) % 10) + lock.substring(3);
                if(!visited.contains(newLockPre) && !deadEndList.contains(newLockPre)){
                    temp.add(newLockPre);
                }
                if(!visited.contains(newLockPost) && !deadEndList.contains(newLockPost)){
                    temp.add(newLockPost);
                }

                int four = NUM_LIST.indexOf(String.valueOf(lock.charAt(3)));
                newLockPre = lock.substring(0,3) + NUM_LIST.get((four - 1 + 10) % 10);
                newLockPost = lock.substring(0,3) + NUM_LIST.get((four + 1 + 10) % 10);
                if(!visited.contains(newLockPre) && !deadEndList.contains(newLockPre)){
                    temp.add(newLockPre);
                }
                if(!visited.contains(newLockPost) && !deadEndList.contains(newLockPost)){
                    temp.add(newLockPost);
                }
            }
            result ++;
            startSet = endSet;
            endSet = temp;
        }
        return -1;
    }
}
