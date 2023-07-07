package com.jyx.feature.test.jdk.algorithm.tree.bst;

import java.io.Serializable;

/**
 * @author Archforce
 * @since 2023/5/30 9:46
 */
public class BstNode<K extends Comparable<K>, V> implements Serializable {

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
    public BstNode<K, V> left;
    /**
     * 右孩子
     */
    public BstNode<K, V> right;

    /**
     * 以该节点为根节点的节点总数
     */
    public int num;

    public BstNode() {

    }

    public BstNode(K key, V value, int num) {
        this.key = key;
        this.value = value;
        this.num = num;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BSTNode(")
                .append("key=").append(key).append(",")
                .append("value=").append(value).append(",")
                .append("num=").append(num)
                .append(")");
        return sb.toString();
    }

}
