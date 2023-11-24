package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.tree.bst.BST;
import com.jyx.feature.test.jdk.algorithm.tree.bst.BstNode;
import com.jyx.feature.test.jdk.algorithm.tree.bst.view.BSTDisplay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangyaxin
 * @since 2023/5/30 10:49
 */
public class BSTTests {

    private static final int SIZE = 20;

    private BST<Integer, Integer> bst;

    private List<BstNode<Integer, Integer>> nodeList;

    private final SecureRandom secureRandom = new SecureRandom();

    private BstNode<Integer, Integer> randomNode() {
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
        bst = new BST<>();
        nodeList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            int value = randomGtZero() % SIZE;
            System.out.println("插入" + value);
            bst.put(value, value);

            nodeList.add(new BstNode<>(value, value, 1));
        }
    }

    @Test
    public void display() throws Exception {
        BSTDisplay.print(bst);
        Thread.currentThread().join();
    }

    @Test
    public void sizeTest() throws Exception {
        BSTDisplay.print(bst);
        System.out.println(bst.size());
        Thread.currentThread().join();
    }

    @Test
    public void getTest() throws Exception {
        BSTDisplay.print(bst);
        System.out.println(bst.get(randomNode().key));
        Thread.currentThread().join();
    }

    @Test
    public void selectTest() throws Exception {
        BSTDisplay.print(bst);
        System.out.println(bst.select(10));
        Thread.currentThread().join();
    }

    @Test
    public void rankTest() throws Exception {
        BSTDisplay.print(bst);
        BstNode<Integer, Integer> targetNode = randomNode();
        System.out.println(targetNode);
        System.out.println(bst.rank(targetNode));
        Thread.currentThread().join();
    }

    @Test
    public void floorTest() throws Exception {
        BSTDisplay.print(bst);
        BstNode<Integer, Integer> targetNode = randomNode();
        System.out.println(targetNode);
        System.out.println(bst.floor(targetNode.key + 1));
        Thread.currentThread().join();
    }

    @Test
    public void cellingTest() throws Exception {
        BSTDisplay.print(bst);
        BstNode<Integer, Integer> targetNode = randomNode();
        System.out.println(targetNode);
        System.out.println(bst.celling(targetNode.key - 1));
        Thread.currentThread().join();
    }

    @Test
    public void minMaxTest() throws Exception {
        BSTDisplay.print(bst);
        System.out.println(bst.min());
        System.out.println(bst.max());
        Thread.currentThread().join();
    }

    @Test
    public void nodesTest() throws Exception {
        BSTDisplay.print(bst);
        System.out.println(bst.keys());
        System.out.println(bst.nodes());
        Thread.currentThread().join();
    }


    @Test
    public void deleteMinTest() throws Exception {
        BSTDisplay.print(bst);
        bst.deleteMin();
        BSTDisplay.print(bst);
        Thread.currentThread().join();
    }

    @Test
    public void deleteTest() throws Exception {
        BSTDisplay.print(bst);
        BstNode<Integer, Integer> targetNode = randomNode();
        System.out.println(targetNode);
        bst.delete(targetNode.key);
        BSTDisplay.print(bst);
        Thread.currentThread().join();
    }

    @Test
    public void foreachTest() throws Exception {
        BSTDisplay.print(bst);
        System.out.println(bst.getDLR());
        System.out.println(bst.getLDR());
        System.out.println(bst.getLRD());
        System.out.println(bst.getHigh());
        System.out.println(bst.getLevelList());
        Thread.currentThread().join();
    }


}
