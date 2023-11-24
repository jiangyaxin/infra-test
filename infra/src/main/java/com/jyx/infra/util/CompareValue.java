package com.jyx.infra.util;

/**
 * @author jiangyaxin
 * @since 2023/10/5 13:47
 */
public class CompareValue<L, R> {

    private final L left;

    private final R right;

    public CompareValue(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
}
