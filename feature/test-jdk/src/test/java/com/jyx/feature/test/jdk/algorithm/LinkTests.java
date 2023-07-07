package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.link.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jiangyaxin
 * @since 2022/4/21 23:14
 */
public class LinkTests {

    @Test
    public void addTwoNumbersTest() {
        assertThat(new L2AddTwoNumbers().addTwoNumbers(
                new ListNode(2, new ListNode(4, new ListNode(3, null))),
                new ListNode(5, new ListNode(6, new ListNode(4, null)))
        ).toString())
                .as("addTwoNumbersTest")
                .isEqualTo(new ListNode(7, new ListNode(0, new ListNode(8, null))).toString());
        assertThat(new L2AddTwoNumbers().addTwoNumbers(
                new ListNode(0, null),
                new ListNode(0, null)
        ).toString())
                .as("addTwoNumbersTest")
                .isEqualTo(new ListNode(0, null).toString());
        assertThat(new L2AddTwoNumbers().addTwoNumbers(
                new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, null))))))),
                new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, null))))
        ).toString())
                .as("addTwoNumbersTest")
                .isEqualTo(new ListNode(8, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(0, new ListNode(0, new ListNode(0, new ListNode(1, null)))))))).toString());
    }


    @Test
    public void mergeTwoListsTest() {
        assertThat(new L21MergeTwoLists().mergeTwoLists(
                new ListNode(1, new ListNode(2, new ListNode(4, null))),
                new ListNode(1, new ListNode(3, new ListNode(4, null)))
        ).toString())
                .as("mergeTwoListsTest")
                .isEqualTo(
                        new ListNode(1,
                                new ListNode(1,
                                        new ListNode(2,
                                                new ListNode(3,
                                                        new ListNode(4,
                                                                new ListNode(4, null)))))).toString());
    }

    @Test
    public void mergeKListsTest() {
        assertThat(new L23MergeKLists().mergeKLists(new ListNode[]{
                new ListNode(1, new ListNode(4, new ListNode(5, null))),
                new ListNode(1, new ListNode(3, new ListNode(4, null))),
                new ListNode(2, new ListNode(6, null))
        }).toString())
                .as("mergeKListsTest")
                .isEqualTo(
                        new ListNode(1,
                                new ListNode(1,
                                        new ListNode(2,
                                                new ListNode(3,
                                                        new ListNode(4,
                                                                new ListNode(4,
                                                                        new ListNode(5,
                                                                                new ListNode(6, null)))))))).toString());
    }

    @Test
    public void hasCycleTest() {
        ListNode listNode1 = new ListNode(2, null);
        listNode1.next = new ListNode(0, new ListNode(-4, listNode1));
        assertThat(new L141HasCycle().hasCycle(new ListNode(3, listNode1)))
                .as("hasCycleTest")
                .isEqualTo(true);
    }

    @Test
    public void detectCycleTest() {
        ListNode listNode1 = new ListNode(2, null);
        listNode1.next = new ListNode(0, new ListNode(-4, listNode1));
        assertThat(new L142DetectCycle().detectCycle(new ListNode(3, listNode1)))
                .as("detectCycleTest")
                .isEqualTo(listNode1);
    }

    @Test
    public void middleNodeTest() {

        ListNode middleNode = new ListNode(3, new ListNode(4, new ListNode(5, null)));
        ListNode listNode = new ListNode(1, new ListNode(2, middleNode));
        assertThat(new L876MiddleNode().middleNode(listNode))
                .as("middleNodeTest")
                .isEqualTo(middleNode);
    }

    @Test
    public void removeNthFromEndTest() {

        assertThat(new L19RemoveNthFromEnd().removeNthFromEnd(new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, null))))), 2).toString())
                .as("removeNthFromEndTest")
                .isEqualTo(new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(5, null)))).toString());
    }

    @Test
    public void getIntersectionNodeTest() {

        ListNode intersectionNode = new ListNode(8, new ListNode(4, new ListNode(5, null)));
        assertThat(new L160GetIntersectionNode().getIntersectionNode(new ListNode(4, new ListNode(1, intersectionNode)), new ListNode(5, new ListNode(6, new ListNode(1, intersectionNode)))))
                .as("getIntersectionNodeTest")
                .isEqualTo(intersectionNode);
    }

    @Test
    public void reverseListTest() {

        assertThat(new L206ReverseList().reverseList(new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, null)))))).toString())
                .as("reverseListTest")
                .isEqualTo(new ListNode(5, new ListNode(4, new ListNode(3, new ListNode(2, new ListNode(1, null))))).toString());
    }

    @Test
    public void reverseBetweenTest() {

        assertThat(new L92ReverseBetween().reverseBetween(new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, null))))), 2, 4).toString())
                .as("reverseBetweenTest")
                .isEqualTo(new ListNode(1, new ListNode(4, new ListNode(3, new ListNode(2, new ListNode(5, null))))).toString());
    }

    @Test
    public void reverseKGroupTest() {

        assertThat(new L25ReverseKGroup().reverseKGroup(new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, null))))), 1).toString())
                .as("reverseKGroupTest")
                .isEqualTo(new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, null))))).toString());

        assertThat(new L25ReverseKGroup().reverseKGroup(new ListNode(1, new ListNode(2, null)), 2).toString())
                .as("reverseKGroupTest")
                .isEqualTo(new ListNode(2, new ListNode(1, null)).toString());

        assertThat(new L25ReverseKGroup().reverseKGroup(new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, null))))), 3).toString())
                .as("reverseKGroupTest")
                .isEqualTo(new ListNode(3, new ListNode(2, new ListNode(1, new ListNode(4, new ListNode(5, null))))).toString());

        assertThat(new L25ReverseKGroup().reverseKGroup(new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, null))))), 2).toString())
                .as("reverseKGroupTest")
                .isEqualTo(new ListNode(2, new ListNode(1, new ListNode(4, new ListNode(3, new ListNode(5, null))))).toString());
    }

    @Test
    public void isPalindromeTest() {

        assertThat(new L234IsPalindrome().isPalindrome(new ListNode(1, new ListNode(2, new ListNode(2, new ListNode(1, null))))))
                .as("isPalindromeTest")
                .isEqualTo(true);
    }

    @Test
    public void deleteDuplicatesTest() {
        assertThat(new L83DeleteDuplicates().deleteDuplicates(new ListNode(1, new ListNode(1, new ListNode(2, null)))))
                .as("deleteDuplicatesTest")
                .isEqualTo(new ListNode(1, new ListNode(2, null)));
        assertThat(new L83DeleteDuplicates().deleteDuplicates(new ListNode(1, new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(3, null)))))))
                .as("deleteDuplicatesTest")
                .isEqualTo(new ListNode(1, new ListNode(2, new ListNode(3, null))));
    }
}
