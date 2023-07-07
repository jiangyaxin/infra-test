package com.jyx.feature.test.jdk.algorithm.bfs;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 力扣 111. 二叉树的最小深度
 * 给定一个二叉树，找出其最小深度。
 *
 * 最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
 *
 * 说明：叶子节点是指没有子节点的节点。
 *
 * 示例 1：
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：2
 *
 * 示例 2：
 * 输入：root = [2,null,3,null,4,null,5,null,6]
 * 输出：5
 *
 * 提示：
 *
 * 树中节点数的范围在 [0, 105] 内
 * -1000 <= Node.val <= 1000
 *
 * @author jiangyaxin
 * @since 2022/4/20 20:19
 */
public class L111MinDepth {

    public int minDepth(TreeNode root) {
        if(root == null){
            return 0;
        }
        Queue<TreeNode> queue = new ArrayBlockingQueue<>(128);
        int result = 1;
        if(root.left == null && root.right == null){
            return result;
        }
        queue.offer(root);
        while (!queue.isEmpty()){
            int size = queue.size();
            for(int i = 0 ; i < size ; i++){
                TreeNode currentNode = queue.poll();
                if(currentNode.left == null && currentNode.right == null){
                    return result;
                }
                if(currentNode.left != null){
                    queue.offer(currentNode.left);
                }
                if(currentNode.right != null){
                    queue.offer(currentNode.right);
                }
            }
            result++;
        }
        return result;
    }

    public static class TreeNode {
         int val;
         TreeNode left;
         TreeNode right;
        public TreeNode() {

         }
        public TreeNode(int val) {
             this.val = val;
         }
         public TreeNode(int val, TreeNode left, TreeNode right) {
             this.val = val;
             this.left = left;
             this.right = right;
         }
    }
}
