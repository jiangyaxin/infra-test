package com.jyx.feature.test.jdk.algorithm.tree.rbt;

import java.util.Objects;
import java.util.Stack;

import static com.jyx.feature.test.jdk.algorithm.tree.rbt.NodeColor.BLACK;
import static com.jyx.feature.test.jdk.algorithm.tree.rbt.NodeColor.RED;

/**
 * @author jiangyaxin
 * @since 2023/5/30 9:46
 */
public class RBT<K extends Comparable<K>, V> {

    /**
     * 红黑树根
     */
    private RbtNode<K, V> root;

    /**
     * 获取红黑树节点数
     */
    public int size() {
        return size(root);
    }

    public int size(RbtNode<K, V> node) {
        if (node == null) {
            return 0;
        } else {
            return node.num;
        }
    }

    /**
     * 根据Key查找节点
     */
    public RbtNode<K, V> get(K key) {
        return get(root, key);
    }

    public RbtNode<K, V> get(RbtNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        RbtNode<K, V> rs = null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            rs = get(node.left, key);
        } else if (cmp > 0) {
            rs = get(node.right, key);
        } else {
            rs = node;
        }
        return rs;
    }

    /**
     * 插入节点
     */
    public void put(K key, V value) {
        root = put(root, key, value);
        root.color = BLACK;
    }

    public RbtNode<K, V> put(RbtNode<K, V> node, K key, V value) {
        if (node == null) {
            return new RbtNode<>(key, value, null, null, 1, RED);
        }

        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else {
            node.value = value;
        }

        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.right) && isRed(node.left)) {
            flipColor(node);
        }

        node.num = 1 + size(node.left) + size(node.right);

        return node;
    }

    public RbtNode<K, V> min() {
        return min(root);
    }

    public RbtNode<K, V> min(RbtNode<K, V> node) {
        Objects.requireNonNull(node);

        Stack<RbtNode<K, V>> stack = new Stack<>();
        stack.push(node);
        while (true) {
            RbtNode<K, V> temp = stack.pop();
            if (temp.left == null) {
                return temp;
            }
            stack.push(temp.left);
        }
    }


    public void deleteMin() {
        Objects.requireNonNull(root);

        //是为了防止在moveRedLeft的交换颜色变成5-节点
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = deleteMin(root);
        //将根节点置为黑色
        if (root != null) {
            root.color = BLACK;
        }
    }

    public RbtNode<K, V> deleteMin(RbtNode<K, V> node) {
        if (node.left == null) {
            return null;
        }
        //如果节点的左子节点是2-节点，需要从左子节点的亲兄弟节点借一个节点
        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }
        //左子节点已经不是2-节点了，进行删除
        node.left = deleteMin(node.left);
        //删除完成后对节点进行重新着色
        return balance(node);
    }

    public void delete(K key) throws Exception {
        Objects.requireNonNull(root);

        //是为了防止在moveRedLeft的交换颜色变成5-节点
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = delete(root, key);
        //将根节点置为黑色
        if (root != null) {
            root.color = BLACK;
        }
    }

    public RbtNode<K, V> delete(RbtNode<K, V> node, K key) throws Exception {
        //保证左子节点不为2-节点  cmp<0表示删除节点位于node的左子树，所以要保证其左子节点不为2-节点
        if (key.compareTo(node.key) < 0) {
            if (node.left == null) {
                throw new NoSuchFieldException(String.format("没有[Key=%s]的节点", key));
            }
            //左子点是2-节点，即左子节点为黑色，左子节点的左子节点为黑色
            if (!isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }
            //去左子树删除
            node.left = delete(node.left, key);
        } else {
            //说明该节点本身就是3-节点, 直接分一个给右子树即可，否则通过下一步左子树借节点，会出现4-节点
            if (isRed(node.left)) {
                node = rotateRight(node);
            }

            //当找到key之后，如果node.right == null表示node是叶子节点，如果key不是叶子节点，需要进行下一步操作
            if (key.compareTo(node.key) == 0 && node.right == null) {
                return null;
            }

            if (!isRed(node.right) && !isRed(node.right.left)) {
                //向左子节点借一个节点给右子节点
                node = moveRedRight(node);
            }

            if (key.compareTo(node.key) == 0) {
                RbtNode<K, V> rightMin = min(node.right);
                node.key = rightMin.key;
                node.value = rightMin.value;
                node.right = deleteMin(node.right);
            } else {
                if (node.right == null) {
                    throw new NoSuchFieldException(String.format("没有[Key=%s]的节点", key));
                }
                node.right = delete(node.right, key);
            }
        }

        return balance(node);

    }

    /**
     * 前提  存在左子节点，并且左子节点是2-节点，所以左子节点为黑色，并且左字节点的左子节点为黑色，隐含条件右子节点定然为黑色
     * 作用  向左子节点的亲兄弟借节点给左子节点，有两种情况   1、亲兄弟是2-节点  2、亲兄弟是3-节点
     */
    private RbtNode<K, V> moveRedLeft(RbtNode<K, V> node) {
        //如果左子节点的亲兄弟是2-节点，则把node节点拉到和两个子节点中，共用一个节点   （包含亲兄弟是3-节点的情况，所以下一步需要处理这种情况）
        flipColor(node);
        //左子节点的亲兄弟是3-节点,做进一步处理
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColor(node);
        }
        return node;
    }

    /**
     * 前提  存在右子节点，并且右子节点是2-节点，右子节点定然为黑色，隐含条件左子节点为黑色
     * 作用  向右子节点的亲兄弟借节点给右子节点，有两种情况   1、亲兄弟是2-节点  2、亲兄弟是3-节点
     */
    private RbtNode<K, V> moveRedRight(RbtNode<K, V> node) {
        //如果右子节点的亲兄弟是2-节点，则把node节点拉到和两个子节点中，共用一个节点   （包含亲兄弟是3-节点的情况，所以下一步需要处理这种情况）
        flipColor(node);
        //右子节点的亲兄弟是3-节点,做进一步处理
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColor(node);
        }

        return node;
    }

    /**
     * 把节点重新着色
     */
    private RbtNode<K, V> balance(RbtNode<K, V> node) {
        if (isRed(node.right)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColor(node);
        }

        node.num = 1 + size(node.left) + size(node.right);
        return node;
    }

    private void flipColor(RbtNode<K, V> node) {
        //变成红色即变为上一层的节点   根节点永远是黑色  所以当根节点由黑色变红时树的高度+1     最后需要把根节点重置为黑色
        node.color = flip(node);
        //变成黑色即变为子节点
        if (node.left != null) {
            node.left.color = flip(node.left);
        }
        if (node.right != null) {
            node.right.color = flip(node.right);
        }
    }

    private NodeColor flip(RbtNode<K, V> node) {
        return node.color.flip();
    }

    /**
     * 节点左旋
     */
    public RbtNode<K, V> rotateLeft(RbtNode<K, V> rbtNode) {
        RbtNode<K, V> tempNode = rbtNode.right;

        rbtNode.right = tempNode.left;
        tempNode.left = rbtNode;

        tempNode.color = rbtNode.color;
        rbtNode.color = RED;

        tempNode.num = rbtNode.num;
        rbtNode.num = 1 + size(rbtNode.left) + size(rbtNode.right);

        return tempNode;
    }

    /**
     * 节点右旋
     */
    public RbtNode<K, V> rotateRight(RbtNode<K, V> rbtNode) {
        RbtNode<K, V> tempNode = rbtNode.left;

        rbtNode.left = tempNode.right;
        tempNode.right = rbtNode;

        tempNode.color = rbtNode.color;
        rbtNode.color = RED;

        tempNode.num = rbtNode.num;
        rbtNode.num = 1 + size(rbtNode.left) + size(rbtNode.right);

        return tempNode;
    }

    public static <K extends Comparable<K>, V> boolean isRed(RbtNode<K, V> rbtNode) {
        if (rbtNode == null) {
            return false;
        }
        return rbtNode.color == RED;
    }

    public RbtNode<K, V> getRoot() {
        return root;
    }
}

