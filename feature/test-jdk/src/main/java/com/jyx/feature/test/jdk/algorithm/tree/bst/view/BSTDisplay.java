package com.jyx.feature.test.jdk.algorithm.tree.bst.view;

import com.jyx.feature.test.jdk.algorithm.tree.bst.BST;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author Archforce
 * @since 2023/5/30 9:46
 */
public class BSTDisplay extends JApplet {

    private static int num = 1;

    public static <K extends Comparable<K>, V> void print(BST<K, V> bst) {
        Objects.requireNonNull(bst);

        JFrame jFrame = new JFrame(num++ + "");
        jFrame.add(new BSTView(bst), BorderLayout.CENTER);
        jFrame.setSize(jFrame.getToolkit().getScreenSize().width, jFrame.getToolkit().getScreenSize().width);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
