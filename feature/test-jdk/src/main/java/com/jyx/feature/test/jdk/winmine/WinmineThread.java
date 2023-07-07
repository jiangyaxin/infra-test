package com.jyx.feature.test.jdk.winmine;

/**
 * @author Archforce
 * @since 2023/7/7 11:34
 */
public class WinmineThread extends Thread {

    public int i;
    public int j;

    public WinmineThread(int i, int j) {
        this.i = i;
        this.j = j;
    }
}
