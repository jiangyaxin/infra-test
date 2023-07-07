package com.jyx.feature.test.jdk.algorithm.tree.bst;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class BST<K extends Comparable<K>, V> {

    private BstNode<K, V> root;

    public int size() {
        return size(this.root);
    }

    public int size(BstNode<K, V> node) {
        if (null == node) {
            return 0;
        } else {
            return node.num;
        }
    }

    public BstNode<K, V> get(K k) {
        return get(this.root, k);
    }

    public BstNode<K, V> get(BstNode<K, V> node, K k) {
        if (node == null) {
            return null;
        }

        BstNode<K, V> rs;
        int cmp = k.compareTo(node.key);
        if (cmp < 0) {
            rs = get(node.left, k);
        } else if (cmp > 0) {
            rs = get(node.right, k);
        } else {
            rs = node;
        }

        return rs;
    }

    public void put(K k, V v) {
        this.root = put(this.root, k, v);
    }

    public BstNode<K, V> put(BstNode<K, V> node, K k, V v) {
        if (node == null) {
            return new BstNode<>(k, v, 1);
        }

        int cmp = k.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, k, v);
        } else if (cmp > 0) {
            node.right = put(node.right, k, v);
        } else {
            node.value = v;
        }

        node.num = calculateNum(node);
        return node;
    }

    /**
     * 排在第k位的节点
     */
    public BstNode<K, V> select(int k) {
        return select(this.root, k);
    }

    /**
     * 在Node中排在第k位的节点
     */
    public BstNode<K, V> select(BstNode<K, V> node, int k) {
        int rootSize = size(node);
        if (rootSize < k) {
            return null;
        }

        BstNode<K, V> rs = null;
        while (node != null) {
            if (size(node.left) >= k) {
                node = node.left;
            } else if ((size(node.left) + 1) < k) {
                k = k - size(node.left) - 1;
                node = node.right;
            } else {
                rs = node;
                break;
            }
        }

        return rs;
    }

    /**
     * targetNode排在第几位
     */
    public int rank(BstNode<K, V> targetNode) {
        return rank(this.root, targetNode);
    }

    /**
     * targetNode 在node中排在第几位
     */
    public int rank(BstNode<K, V> node, BstNode<K, V> targetNode) {
        int k = 0;

        Stack<BstNode<K, V>> stack = new Stack<>();

        while (node != null || !stack.empty()) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }

            if (!stack.empty()) {
                BstNode<K, V> stackPeek = stack.pop();
                k++;

                if (stackPeek.key.compareTo(targetNode.key) == 0) {
                    break;
                }
                node = stackPeek.right;

            }
        }

        if (k == 0) {
            throw new NoSuchElementException(targetNode.toString());
        }

        return k;
    }

    /**
     * 小于等于key 的最大键
     */
    public BstNode<K, V> floor(K k) {
        return floor(this.root, k);
    }

    /**
     * node中 小于等于key 的最大键
     */
    public BstNode<K, V> floor(BstNode<K, V> node, K k) {
        if (node == null) {
            return null;
        }

        int cmp = k.compareTo(node.key);
        if (cmp == 0) {
            return node;
        }
        if (cmp < 0) {
            return floor(node.left, k);
        }

        BstNode<K, V> t = floor(node.right, k);
        if (t == null) {
            return node;
        } else {
            return t;
        }


    }

    /**
     * 大于等于key 的最大键
     */
    public BstNode<K, V> celling(K k) {
        return celling(this.root, k);
    }

    /**
     * node中 大于等于key 的最大键
     */
    public BstNode<K, V> celling(BstNode<K, V> node, K k) {
        if (node == null) {
            return null;
        }

        int cmp = k.compareTo(node.key);
        if (cmp == 0) {
            return node;
        }
        if (cmp > 0) {
            return celling(node.right, k);
        }

        BstNode<K, V> t = celling(node.left, k);

        if (t == null) {
            return node;
        } else {
            return t;
        }

    }


    public BstNode<K, V> min() {
        return min(this.root);
    }

    public BstNode<K, V> min(BstNode<K, V> node) {
        if (node.left == null) {
            return node;
        }

        return min(node.left);
    }

    public BstNode<K, V> max() {
        return max(this.root);
    }

    public BstNode<K, V> max(BstNode<K, V> node) {
        if (node.right == null) {
            return node;
        }

        return max(node.right);
    }

    public void deleteMin() {
        this.root = deleteMin(this.root);
    }

    public BstNode<K, V> deleteMin(BstNode<K, V> node) {
        if (node.left == null) {
            return node.right;
        }

        node.left = deleteMin(node.left);

        node.num = calculateNum(node);

        return node;
    }

    public void delete(K k) throws CloneNotSupportedException {
        this.root = delete(this.root, k);
    }

    public BstNode<K, V> delete(BstNode<K, V> node, K k) throws CloneNotSupportedException {
        if (node == null) {
            return null;
        }

        int cmp = k.compareTo(node.key);

        if (cmp < 0) {
            node.left = delete(node.left, k);
        } else if (cmp > 0) {
            node.right = delete(node.right, k);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            BstNode<K, V> rightMinNode = min(node.right);
            rightMinNode.left = node.left;
            rightMinNode.right = deleteMin(node.right);

            node = rightMinNode;
        }
        return node;
    }

    public List<K> keys() {
        return keys(min().key, max().key);
    }

    public List<K> keys(K lo, K hi) {
        return keys(this.root, lo, hi);
    }

    public List<K> keys(BstNode<K, V> node, K lo, K hi) {
        List<BstNode<K, V>> nodeList = this.nodes(node, lo, hi);

        List<K> rs = new ArrayList<>(nodeList.size());
        for (BstNode<K, V> bstNode : nodeList) {
            rs.add(bstNode.key);
        }
        return rs;
    }

    public List<BstNode<K, V>> nodes() {
        return nodes(min().key, max().key);
    }

    public List<BstNode<K, V>> nodes(K lo, K hi) {
        return nodes(this.root, lo, hi);
    }

    public List<BstNode<K, V>> nodes(BstNode<K, V> node, K lo, K hi) {
        List<BstNode<K, V>> rs = new ArrayList<>();
        Stack<BstNode<K, V>> stack = new Stack<>();

        while (node != null || !stack.empty()) {
            while (node != null) {
                if (hi.compareTo(node.key) < 0) {
                    node = node.left;
                    continue;
                }

                if (lo.compareTo(node.key) > 0) {
                    node = node.right;
                    continue;
                }
                stack.push(node);
                node = node.left;
            }

            if (!stack.empty()) {
                BstNode<K, V> stackPeek = stack.pop();
                rs.add(stackPeek);

                node = stackPeek.right;
            }
        }

        return rs;
    }


    /**
     * 中序遍历
     */
    public List<BstNode<K, V>> getLDR() {
        return getLDR(this.root);
    }

    /**
     * 中序遍历
     */
    public List<BstNode<K, V>> getLDR(BstNode<K, V> node) {
        List<BstNode<K, V>> rs = new ArrayList<>();
        Stack<BstNode<K, V>> stack = new Stack<>();

        while (node != null || !stack.empty()) {

            while (node != null) {
                stack.push(node);
                node = node.left;
            }

            if (!stack.empty()) {
                BstNode<K, V> stackPeek = stack.pop();
                rs.add(stackPeek);

                node = stackPeek.right;

            }
        }
        return rs;
    }

    /**
     * 前序遍历
     */
    public List<BstNode<K, V>> getDLR() {
        return getDLR(this.root);
    }

    /**
     * 前序遍历
     */
    public List<BstNode<K, V>> getDLR(BstNode<K, V> node) {
        List<BstNode<K, V>> rs = new ArrayList<>();
        Stack<BstNode<K, V>> stack = new Stack<>();

        while (node != null || !stack.empty()) {

            while (node != null) {
                rs.add(node);
                stack.push(node);
                node = node.left;
            }

            if (!stack.empty()) {
                BstNode<K, V> stackPeek = stack.pop();
                node = stackPeek.right;

            }
        }
        return rs;
    }

    /**
     * 后序遍历
     */
    public List<BstNode<K, V>> getLRD() {
        return getLRD(this.root);
    }

    /**
     * 后序遍历
     */
    public List<BstNode<K, V>> getLRD(BstNode<K, V> node) {
        List<BstNode<K, V>> rs = new ArrayList<>();
        Stack<BstNode<K, V>> stack1 = new Stack<>();
        Stack<BstNode<K, V>> stack2 = new Stack<>();

        stack1.push(node);

        while (!stack1.empty()) {
            BstNode<K, V> temp = stack1.pop();

            stack2.push(temp);

            if (temp.left != null) {
                stack1.push(temp.left);
            }

            if (temp.right != null) {
                stack1.push(temp.right);
            }
        }

        while (!stack2.empty()) {
            rs.add(stack2.pop());
        }

        return rs;
    }


    public int getHigh() {
        return getLevelList().size();
    }

    public int getHigh(BstNode<K, V> node) {
        return getLevelList(node).size();
    }

    /**
     * 层序遍历
     */
    public LinkedList<LinkedList<BstNode<K, V>>> getLevelList() {
        return getLevelList(this.root);
    }

    /**
     * 层序遍历
     * high  靠近根节点的高度  根节点的高度为0
     */
    private LinkedList<LinkedList<BstNode<K, V>>> getLevelList(BstNode<K, V> node) {
        LinkedList<LinkedList<BstNode<K, V>>> rs = new LinkedList<>();
        LinkedBlockingQueue<BstNode<K, V>> queue = new LinkedBlockingQueue<>();
        queue.offer(node);
        int currentRowNum = 0;
        //该行还剩几个值queue中
        int currentRowInQuene = 1;
        //下一行还剩几个值位于queue中
        int nextRowInQuene = 0;

        while (queue.peek() != null) {
            BstNode<K, V> queueHead = queue.poll();
            currentRowInQuene--;

            try {
                rs.get(currentRowNum);
            } catch (Exception e) {
                rs.add(currentRowNum, new LinkedList<>());
            }


            rs.get(currentRowNum).add(queueHead);

            if (queueHead.left != null) {
                queue.offer(queueHead.left);
                nextRowInQuene++;
            }
            if (queueHead.right != null) {
                queue.offer(queueHead.right);
                nextRowInQuene++;
            }

            if (currentRowInQuene == 0) {
                currentRowInQuene = nextRowInQuene;
                nextRowInQuene = 0;
                currentRowNum++;
            }
        }

        return rs;
    }

    private int calculateNum(BstNode<K, V> node) {
        return size(node.left) + size(node.right) + 1;
    }

    public BstNode<K, V> getRoot() {
        return root;
    }

}
