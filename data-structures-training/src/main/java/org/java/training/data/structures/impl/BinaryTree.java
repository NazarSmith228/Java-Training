package org.java.training.data.structures.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BinaryTree<T extends Comparable<? super T>> {

    TreeNode root;
    int size;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private class TreeNode {
        final T value;
        TreeNode left;
        TreeNode right;

        public TreeNode(T value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public void insert(T value) {
        root = insertVal(root, value);
    }

    private TreeNode insertVal(TreeNode root, T value) {
        if (root == null) {
            size++;
            return new TreeNode(value);
        }

        if (root.value.compareTo(value) > 0) {
            root.left = insertVal(root.left, value);
        } else if (root.value.compareTo(value) < 0) {
            root.right = insertVal(root.right, value);
        } else {
            return root;
        }

        return root;
    }

    public int size() {
        return size;
    }

    public boolean contains(T value) {
        return containsVal(root, value);
    }

    private boolean containsVal(TreeNode root, T value) {
        if (root == null) {
            return false;
        } else if (root.value.compareTo(value) == 0) {
            return true;
        } else {
            return root.value.compareTo(value) > 0
                    ? containsVal(root.left, value)
                    : containsVal(root.right, value);
        }
    }

    public void traverseAscending() {
        traverseAscending(root, System.out::print);
    }

    private void traverseAscending(TreeNode root, Consumer<TreeNode> nodeConsumer) {
        if (root != null) {
            traverseAscending(root.left, nodeConsumer);
            nodeConsumer.andThen((treeNode) -> System.out.print("->")).accept(root);
            traverseAscending(root.right, nodeConsumer);
        }
    }

    public int depth() {
        return depth(root, 0) - 1;
    }

    private int depth(TreeNode root, int count) {
        if (root == null) {
            return count;
        }
        return Math.max(depth(root.left, count + 1), depth(root.right, count + 1));
    }

    public String prettyPrintTree() {
        if (root == null) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(root.value);

        String pointerLeft = "├──";
        String pointerRight = (root.left != null) ? "├──" : "└──";

        prettyPrintNodes(sb, StringUtils.EMPTY, pointerLeft, root.right, root.left != null);
        prettyPrintNodes(sb, StringUtils.EMPTY, pointerRight, root.left, false);

        return sb.toString();
    }

    private void prettyPrintNodes(StringBuilder sb, String initPadding, String nodePointer, TreeNode node,
                                  boolean hasLeft) {
        if (node != null) {
            sb.append("\n");
            sb.append(initPadding);
            sb.append(nodePointer);
            sb.append(node.value);

            StringBuilder paddingBuilder = new StringBuilder(initPadding);
            if (hasLeft) {
                paddingBuilder.append("│".concat(StringUtils.SPACE.repeat(2)));
            } else {
                paddingBuilder.append(StringUtils.SPACE.repeat(3));
            }
            String paddingForBoth = paddingBuilder.toString();
            String pointerLeft = "├──";
            String pointerRight = (node.left != null) ? "├──" : "└──";

            prettyPrintNodes(sb, paddingForBoth, pointerLeft, node.right, node.left != null);
            prettyPrintNodes(sb, paddingForBoth, pointerRight, node.left, false);
        }
    }
}
