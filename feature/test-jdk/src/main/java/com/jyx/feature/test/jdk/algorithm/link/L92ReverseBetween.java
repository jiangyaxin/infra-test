package com.jyx.feature.test.jdk.algorithm.link;

/**
 * 力扣 92. 反转链表 II
 * 给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right 。请你反转从位置 left 到位置 right 的链表节点，返回 反转后的链表 。
 *
 * 示例 1：
 * 输入：head = [1,2,3,4,5], left = 2, right = 4
 * 输出：[1,4,3,2,5]
 *
 * 示例 2：
 * 输入：head = [5], left = 1, right = 1
 * 输出：[5]
 *
 * 提示：
 * 链表中节点数目为 n
 * 1 <= n <= 500
 * -500 <= Node.val <= 500
 * 1 <= left <= right <= n
 *
 * @author jiangyaxin
 * @since 2022/4/25 20:13
 */
public class L92ReverseBetween {

    private ListNode post = null;

    public ListNode reverseN(ListNode head,int n){
        if( n == 1 ){
            post = head.next;
            return head;
        }

        ListNode last = reverseN(head.next,n-1);
        head.next.next = head;
        head.next = post;
        return last;
    }


    public ListNode reverseBetween(ListNode head, int left, int right) {
        if(left == 1){
            return reverseN(head,right);
        }

        head.next = reverseBetween(head.next,left-1,right-1);

        return head;
    }

}
