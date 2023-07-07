package com.jyx.feature.test.jdk.algorithm.tree.rbt.view;


import com.jyx.feature.test.jdk.algorithm.tree.rbt.RBT;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author Archforce
 * @since 2023/5/30 9:46
 */
public class RBTDisplay extends JApplet {

    private static int num = 1;

    public static <K extends Comparable<K>, V> void print(RBT<K, V> rbt) {
        Objects.requireNonNull(rbt);

        JFrame jFrame = new JFrame(num++ + "");
        jFrame.add(new RBTView(rbt), BorderLayout.CENTER);
        jFrame.setSize(jFrame.getToolkit().getScreenSize().width, jFrame.getToolkit().getScreenSize().width);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
