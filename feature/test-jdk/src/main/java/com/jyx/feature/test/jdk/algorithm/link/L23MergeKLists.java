package com.jyx.feature.test.jdk.algorithm.link;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 力扣 23. 合并K个升序链表
 * 给你一个链表数组，每个链表都已经按升序排列。
 *
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 *
 * 示例 1：
 * 输入：lists = [[1,4,5],[1,3,4],[2,6]]
 * 输出：[1,1,2,3,4,4,5,6]
 * 解释：链表数组如下：
 * [
 *   1->4->5,
 *   1->3->4,
 *   2->6
 * ]
 * 将它们合并到一个有序链表中得到。
 * 1->1->2->3->4->4->5->6
 *
 * 示例 2：
 * 输入：lists = []
 * 输出：[]
 *
 * 示例 3：
 * 输入：lists = [[]]
 * 输出：[]
 *
 * 提示：
 * k == lists.length
 * 0 <= k <= 10^4
 * 0 <= lists[i].length <= 500
 * -10^4 <= lists[i][j] <= 10^4
 * lists[i] 按 升序 排列
 * lists[i].length 的总和不超过 10^4
 *
 * @author jiangyaxin
 * @since 2022/4/24 20:23
 */
public class L23MergeKLists {

    public ListNode mergeKLists(ListNode[] lists) {
        ListNode head = new ListNode();
        ListNode headPointer = head;
        if(lists.length == 0){
            return head.next;
        }

        PriorityQueue<ListNode> queue = new PriorityQueue<>(lists.length, Comparator.comparingInt(o -> o.val));
        for (ListNode listNode : lists){
            if(listNode != null) {
                queue.offer(listNode);
            }
        }

        while (!queue.isEmpty()){
            ListNode minListNode = queue.poll();
            headPointer.next = minListNode;
            headPointer = headPointer.next;

            if(minListNode.next !=null) {
                minListNode = minListNode.next;
                queue.offer(minListNode);
            }
        }

        return head.next;
    }

}
