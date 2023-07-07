package com.jyx.feature.test.jdk.algorithm.tree.bst.view;


import com.jyx.feature.test.jdk.algorithm.tree.View;
import com.jyx.feature.test.jdk.algorithm.tree.bst.BST;
import com.jyx.feature.test.jdk.algorithm.tree.bst.BstNode;

import javax.swing.*;
import java.awt.*;

/**
 * @author Archforce
 * @since 2023/5/30 9:46
 */
public class BSTView<K extends Comparable<K>, V> extends JPanel implements View {
    private final BstNode<K, V> root;

    public BSTView(BST<K, V> root) {
        this.root = (BstNode<K, V>) clone(root.getRoot());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (root != null) {
            display(g, root, getWidth() / 2, 30, getWidth() / 4);
        }
    }

    private void display(Graphics g, BstNode<K, V> node, int x, int y, int hGap) {
        g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
        g.drawString(node.key.toString(), x - 6, y + 4);
        if (node.left != null) {
            connectLeftChild(g, x - hGap, y + offset, x, y);
            display(g, node.left, x - hGap, y + offset, hGap / 2);
        }
        if (node.right != null) {
            connectRightChild(g, x + hGap, y + offset, x, y);
            display(g, node.right, x + hGap, y + offset, hGap / 2);
        }
    }
}

