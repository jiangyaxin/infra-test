package com.jyx.feature.test.jdk.algorithm.tree;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Archforce
 * @since 2023/5/30 13:43
 */
public interface View {

    int radius = 20;

    int offset = 50;

    default Object clone(Object root) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(root);

            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            return oi.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default void connectRightChild(Graphics g, int x1, int y1, int x2, int y2) {
        double d = Math.sqrt(offset * offset + (x2 - x1) * (x2 - x1));
        int x11 = (int) (x1 - radius * (x1 - x2) / d);
        int y11 = (int) (y1 - radius * offset / d);
        int x21 = (int) (x2 + radius * (x1 - x2) / d);
        int y21 = (int) (y2 + radius * offset / d);
        g.drawLine(x11, y11, x21, y21);
    }

    default void connectLeftChild(Graphics g, int x1, int y1, int x2, int y2) {
        double d = Math.sqrt(offset * offset + (x2 - x1) * (x2 - x1));
        int x11 = (int) (x1 + radius * (x2 - x1) / d);
        int y11 = (int) (y1 - radius * offset / d);
        int x21 = (int) (x2 - radius * (x2 - x1) / d);
        int y21 = (int) (y2 + radius * offset / d);
        g.drawLine(x11, y11, x21, y21);
    }
}
