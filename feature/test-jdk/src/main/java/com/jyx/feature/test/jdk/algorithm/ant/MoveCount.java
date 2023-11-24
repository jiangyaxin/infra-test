package com.jyx.feature.test.jdk.algorithm.ant;

/**
 * @author jiangyaxin
 * @since 2021/1/29 10:24
 */
public class MoveCount {
    private int times;

    public MoveCount(){
        this.times = 0;
    }

    public boolean reach(int K){
        return this.times == K;
    }

    public void addStep(){
        this.times++;
    }
}
