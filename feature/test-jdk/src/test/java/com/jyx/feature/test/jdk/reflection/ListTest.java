package com.jyx.feature.test.jdk.reflection;

import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Archforce
 * @since 2023/4/3 10:43
 */
public class ListTest {

    private static final int length = 1000_0000;

    @Test
    public  void arrayListTest() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Integer> result = new ArrayList<>(length+1);
        add(result);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    @Test
    public void linkedListTest() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Integer> result = new LinkedList<>();
        add(result);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    private void add(List<Integer> list) {
       for(int i=0 ; i < length ; i++) {
           list.add(i);
       }
    }
}
