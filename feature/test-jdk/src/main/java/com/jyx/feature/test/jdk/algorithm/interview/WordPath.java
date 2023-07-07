package com.jyx.feature.test.jdk.algorithm.interview;

import java.util.Scanner;

/**
 * 找出目标单词在字母矩阵中是否存在，可以上下左右走，但不能走回头路
 *
 * 例如：
 * 5 5
 * HELLOWORLD
 * AAAAA
 * AAAAA
 * AAAAA
 * AAAAA
 * AAAAA
 *
 * @author jiangyaxin
 * @since 2022/7/20 20:55
 */
public class WordPath {

    private static int n;

    private static int m;

    private static String targetWord;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        m = in.nextInt();
        in.nextLine();
        targetWord = in.nextLine();

        String[] metrix = new String[n];
        for(int i = 0 ; i < n ; i++){
            metrix[i] = in.nextLine();
        }

        String firstChar = charAt(targetWord, 0);

        for(int i = 0 ; i < n ; i++){
            String line = metrix[i];
            if(line.contains(firstChar)){
                int j = line.indexOf(firstChar);
                boolean[][] visited = new boolean[n][m];
                visited[i][j] = true;
                if(findTargetWord(metrix,visited,i,j,1)){
                    System.out.printf("%s %s",i+1,j+1);
                    return;
                }
            }
        }
        System.out.println("NO");
    }

    public static String charAt(String str,int index){
        return String.valueOf(str.charAt(index));
    }

    public static boolean findTargetWord(String[] metrix,boolean[][] visited,int i,int j,int index){
        if(index == targetWord.length()){
            return true;
        }

        if(i > 0 && !visited[i-1][j] && metrix[i-1].charAt(j) == targetWord.charAt(index)){
            visited[i-1][j] = true;
            if(findTargetWord(metrix,visited,i-1,j,index+1)){
                return true;
            }
            visited[i-1][j] = false;
        }

        if(j > 0 && !visited[i][j-1] && metrix[i].charAt(j-1) == targetWord.charAt(index)){
            visited[i][j-1] = true;
            if(findTargetWord(metrix,visited,i,j-1,index+1)){
                return true;
            }
            visited[i][j-1] = false;
        }

        if(i < n-1 && !visited[i+1][j] && metrix[i+1].charAt(j) == targetWord.charAt(index)){
            visited[i+1][j] = true;
            if(findTargetWord(metrix,visited,i+1,j,index+1)){
                return true;
            }
            visited[i+1][j] = false;
        }

        if(j < m-1 && !visited[i][j+1] && metrix[i].charAt(j+1) == targetWord.charAt(index)){
            visited[i][j+1] = true;
            if(findTargetWord(metrix,visited,i,j+1,index+1)){
                return true;
            }
            visited[i][j+1] = false;
        }

        return false;
    }
}
