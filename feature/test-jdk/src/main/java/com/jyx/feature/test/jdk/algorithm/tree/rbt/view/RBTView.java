package com.jyx.feature.test.jdk.algorithm.tree.rbt.view;

import com.jyx.feature.test.jdk.algorithm.tree.View;
import com.jyx.feature.test.jdk.algorithm.tree.rbt.RBT;
import com.jyx.feature.test.jdk.algorithm.tree.rbt.RbtNode;

import javax.swing.*;
import java.awt.*;

public class RBTView<K extends Comparable<K>, V> extends JPanel implements View {
    private final RbtNode<K, V> root;

    public RBTView(RBT<K, V> bst) {
        this.root = (RbtNode<K, V>) clone(bst.getRoot());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        display(g, root, getWidth() / 2, 30, getWidth() / 4);
    }

    private void display(Graphics g, RbtNode<K, V> node, int x, int y, int hGap) {
        g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
        g.drawString(node.key.toString(), x - 6, y + 4);
        g.setColor(Color.BLACK);
        if (node.left != null) {
            if (RBT.isRed(node.left)) {
                g.setColor(Color.RED);
            }
            connectLeftChild(g, x - hGap, y + vGap, x, y);

            display(g, node.left, x - hGap, y + vGap, hGap / 2);
        }
        if (node.right != null) {
            if (RBT.isRed(node.right)) {
                g.setColor(Color.RED);
            }
            connectRightChild(g, x + hGap, y + vGap, x, y);
            display(g, node.right, x + hGap, y + vGap, hGap / 2);
        }
    }
}

