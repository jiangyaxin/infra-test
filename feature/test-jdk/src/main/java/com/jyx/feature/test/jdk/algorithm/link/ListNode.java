package com.jyx.feature.test.jdk.algorithm.link;

import lombok.EqualsAndHashCode;

/**
 * @author jiangyaxin
 * @since 2022/4/24 20:25
 */
@EqualsAndHashCode
public class ListNode {

    public int val;

    public ListNode next;

    public ListNode() {

    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ListNode p = this; p != null; p = p.next) {
            sb.append(p.val);
        }
        return sb.toString();
    }

}
