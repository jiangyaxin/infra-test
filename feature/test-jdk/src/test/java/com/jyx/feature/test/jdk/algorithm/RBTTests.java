package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.tree.rbt.NodeColor;
import com.jyx.feature.test.jdk.algorithm.tree.rbt.RBT;
import com.jyx.feature.test.jdk.algorithm.tree.rbt.RbtNode;
import com.jyx.feature.test.jdk.algorithm.tree.rbt.view.RBTDisplay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Archforce
 * @since 2023/5/30 10:49
 */
public class RBTTests {

    private static final int SIZE = 100;

    private RBT<Integer, Integer> rbt;

    private List<RbtNode<Integer, Integer>> nodeList;

    private Set<Integer> keySet;

    private final SecureRandom secureRandom = new SecureRandom();

    private RbtNode<Integer, Integer> randomNode() {
        int randomIndex = randomGtZero() % nodeList.size();
        return nodeList.get(randomIndex);
    }

    private int randomGtZero() {
        int seed = -1;
        while (seed < 0) {
            seed = secureRandom.nextInt();
        }
        return seed;
    }

    @BeforeEach
    public void init() {
        rbt = new RBT<>();
        nodeList = new ArrayList<>(SIZE);
        keySet = new HashSet<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            int value = randomGtZero() % SIZE;
            System.out.println("插入" + value);
            rbt.put(value, value);
            keySet.add(value);

            nodeList.add(new RbtNode<>(value, value, null, null, 1, NodeColor.BLACK));
        }
    }

    @Test
    public void display() throws Exception {
        RBTDisplay.print(rbt);
        Thread.currentThread().join();
    }

    @Test
    public void sizeTest() throws Exception {
        RBTDisplay.print(rbt);
        System.out.println("key num is " + keySet.size());
        System.out.println(rbt.size());
        Thread.currentThread().join();
    }

    @Test
    public void getTest() throws Exception {
        RBTDisplay.print(rbt);
        Integer key = randomNode().key;
        System.out.println(key);
        System.out.println(rbt.get(key));
        Thread.currentThread().join();
    }

    @Test
    public void minTest() throws Exception {
        RBTDisplay.print(rbt);
        System.out.println(rbt.min());
        Thread.currentThread().join();
    }

    @Test
    public void deleteTest() throws Exception {
        RBTDisplay.print(rbt);
        RbtNode<Integer, Integer> targetNode = randomNode();
        System.out.println(targetNode);
        rbt.delete(targetNode.key);
        RBTDisplay.print(rbt);
        Thread.currentThread().join();
    }


}
