package com.jyx.feature.test.jdk.algorithm.link;

/**
 * 力扣 19. 删除链表的倒数第 N 个结点
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 *
 * 示例 1：
 * 输入：head = [1,2,3,4,5], n = 2
 * 输出：[1,2,3,5]
 *
 * 示例 2：
 * 输入：head = [1], n = 1
 * 输出：[]
 *
 * 示例 3：
 * 输入：head = [1,2], n = 1
 * 输出：[1]
 *
 * 提示：
 * 链表中结点的数目为 sz
 * 1 <= sz <= 30
 * 0 <= Node.val <= 100
 * 1 <= n <= sz
 *
 * @author jiangyaxin
 * @since 2022/4/24 21:29
 */
public class L19RemoveNthFromEnd {

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode temp = new ListNode(-1,head);
        int numFromEnd = remove(temp, temp.next, n) + 1;
        if(numFromEnd == n){
            head.next = head.next.next;
        }
        return temp.next;
    }

    public int remove(ListNode current,ListNode post, int n) {
        if( post == null){
            return 0;
        }

        int numFromEnd = remove(post,post.next, n) + 1;

        if(numFromEnd == n){
            current.next = post.next;
        }
        return numFromEnd;
    }

}
