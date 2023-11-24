package com.jyx.feature.test.jdk.algorithm.tree.rbt;


import java.io.Serializable;

/**
 * @author jiangyaxin
 * @since 2023/5/30 9:46
 */
public class RbtNode<K extends Comparable<K>, V> implements Serializable {
    /**
     * 键
     */
    public K key;
    /**
     * 值
     */
    public V value;
    /**
     * 左孩子
     */
    public RbtNode<K, V> left;
    /**
     * 右孩子
     */
    public RbtNode<K, V> right;

    /**
     * 以该节点为根节点的节点总数
     */
    public int num;

    /**
     * 由父节点指向它链接的颜色
     */
    public NodeColor color;

    public RbtNode(K key, V value, RbtNode<K, V> left, RbtNode<K, V> right, int num, NodeColor color) {
        this.key = key;
        this.value = value;
        this.left = left;
        this.right = right;
        this.num = num;
        this.color = color;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RbtNode(")
                .append("key=").append(key).append(",")
                .append("value=").append(value).append(",")
                .append("num=").append(num).append(",")
                .append("color=").append(color.name())
                .append(")");
        return sb.toString();
    }
}