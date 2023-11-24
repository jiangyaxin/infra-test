package com.jyx.feature.test.jdk.algorithm.link;

/**
 * 力扣 2. 两数相加
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 * <p>
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * <p>
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * <p>
 * 示例 1：
 * 输入：l1 = [2,4,3], l2 = [5,6,4]
 * 输出：[7,0,8]
 * 解释：342 + 465 = 807.
 * <p>
 * 示例 2：
 * 输入：l1 = [0], l2 = [0]
 * 输出：[0]
 * <p>
 * 示例 3：
 * 输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
 * 输出：[8,9,9,9,0,0,0,1]
 * <p>
 * 提示：
 * 每个链表中的节点数在范围 [1, 100] 内
 * 0 <= Node.val <= 9
 * 题目数据保证列表表示的数字不含前导零
 *
 * @author jiangyaxin
 * @since 2023/6/12 17:30
 */
public class L2AddTwoNumbers {

    private final ListNode head = new ListNode();

    private ListNode tail = head;

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        calculate(l1, l2,0);

        return head.next;
    }

    private void calculate(ListNode l1, ListNode l2,int up) {
        int val1 = l1 == null ? 0 : l1.val;
        int val2 = l2 == null ? 0 : l2.val;
        int val = val1 + val2 + up;
        up = val / 10;

        tail.next = new ListNode(val % 10, null);
        tail = tail.next;

        ListNode next1 = next(l1);
        ListNode next2 = next(l2);

        if (next1 == null && next2 == null && up == 0) {
            return;
        }

        calculate(next1, next2,up);
    }

    private ListNode next(ListNode node) {
        if (node == null) {
            return null;
        }
        return node.next == null ? null : node.next;
    }
}
