package com.jyx.feature.test.jdk.algorithm.link;

/**
 * 力扣 25. K 个一组翻转链表
 * 给你链表的头节点 head ，每 k 个节点一组进行翻转，请你返回修改后的链表。
 *
 * k 是一个正整数，它的值小于或等于链表的长度。如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 *
 * 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
 *
 * 示例 1：
 * 输入：head = [1,2,3,4,5], k = 2
 * 输出：[2,1,4,3,5]
 *
 * 示例 2：
 * 输入：head = [1,2,3,4,5], k = 3
 * 输出：[3,2,1,4,5]
 *
 * 提示：
 * 链表中的节点数目为 n
 * 1 <= k <= n <= 5000
 * 0 <= Node.val <= 1000
 *
 * @author jiangyaxin
 * @since 2022/4/25 20:50
 */
public class L25ReverseKGroup {

    public ListNode  reverseKGroup(ListNode head, int k) {
        ListNode left = head;
        ListNode right = head;
        for(int i = 0 ; i < k ; i++){
            if(right.next == null && i < k-1){
                return head;
            }
            right = right.next;
        }
        ListNode newHead = reverseLeft(left, right);
        if(right != null) {
            left.next = reverseKGroup(right, k);
        }
        return newHead;
    }

    private ListNode reverseLeft(ListNode left , ListNode right){
        ListNode pre = null;
        ListNode mid = left;
        ListNode end = left.next;
        while (mid != right){
            mid.next = pre;
            pre = mid;
            if(end == null){
                break;
            }
            mid = end;
            end = end.next;
        }
        left.next = right;
        return pre;
    }

}
