package com.jyx.feature.test.jdk.algorithm.link;

/**
 * 力扣 234. 回文链表
 * 给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。
 *
 * 示例 1：
 * 输入：head = [1,2,2,1]
 * 输出：true
 *
 * 示例 2：
 * 输入：head = [1,2]
 * 输出：false
 *
 * 提示：
 * 链表中节点数目在范围[1, 105] 内
 * 0 <= Node.val <= 9
 *
 * @author jiangyaxin
 * @since 2022/4/26 20:32
 */
public class L234IsPalindrome {

    private ListNode left;

    public boolean isPalindrome(ListNode head) {
        left = head;
        return recursion(head);
    }

    public boolean recursion(ListNode head){
        if(head == null){
            return true;
        }
        boolean result = recursion(head.next);
        result = result & (left.val == head.val);
        left = left.next;
        return result;
    }
}
