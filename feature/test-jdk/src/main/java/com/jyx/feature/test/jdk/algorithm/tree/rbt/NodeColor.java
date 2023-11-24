package com.jyx.feature.test.jdk.algorithm.tree.rbt;

/**
 * @author jiangyaxin
 * @since 2023/5/30 12:48
 */
public enum NodeColor {

    RED(true),

    BLACK(false);

    public final boolean color;

    NodeColor(boolean color) {
        this.color = color;
    }

    public NodeColor flip() {
        return color ? BLACK : RED;
    }
}
