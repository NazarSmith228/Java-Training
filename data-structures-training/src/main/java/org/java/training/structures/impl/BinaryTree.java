package org.java.training.structures.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.apache.commons.lang3.CharUtils.LF;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BinaryTree<T extends Comparable<? super T>> {

    TreeNode<T> root;

    int size;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class TreeNode<T> {
        final T value;
        TreeNode<T> left;
        TreeNode<T> right;

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

    @SafeVarargs
    public static <T extends Comparable<? super T>> BinaryTree<T> of(T... elements) {
        BinaryTree<T> tree = new BinaryTree<>();
        Stream.of(elements).forEach(tree::insert);
        return tree;
    }

    public boolean insert(T value) {
        Objects.requireNonNull(value);

        boolean inserted = insertVal(value);
        if (inserted) {
            size++;
        }
        return inserted;
    }

    private boolean insertVal(T value) {
        if (root == null) {
            root = new TreeNode<>(value);
            return true;
        }
        return insertIntoSubTree(root, value);
    }

    private boolean insertLeft(TreeNode<T> node, T value) {
        if (node.left == null) {
            node.left = new TreeNode<>(value);
            return true;
        }
        return insertIntoSubTree(node.left, value);
    }

    private boolean insertRight(TreeNode<T> node, T value) {
        if (node.right == null) {
            node.right = new TreeNode<>(value);
            return true;
        }
        return insertIntoSubTree(node.right, value);
    }

    private boolean insertIntoSubTree(TreeNode<T> current, T value) {
        T existing = current.value;
        if (existing.compareTo(value) > 0) {
            return insertLeft(current, value);
        } else if (existing.compareTo(value) < 0) {
            return insertRight(current, value);
        } else {
            return false;
        }
    }

    //todo: implement delete method with re-balancing
    public boolean delete(T value) {
        return false;
    }

    public int size() {
        return size;
    }

    public boolean contains(T value) {
        Objects.requireNonNull(value);
        return containsVal(root, value);
    }

    private boolean containsVal(TreeNode<T> root, T value) {
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

    public void traverseAscending(Consumer<T> nodeConsumer) {
        traverseAscending(root, nodeConsumer);
    }

    private void traverseAscending(TreeNode<T> root, Consumer<T> nodeConsumer) {
        if (root != null) {
            traverseAscending(root.left, nodeConsumer);
            nodeConsumer.andThen((val) -> System.out.print("->")).accept(root.value);
            traverseAscending(root.right, nodeConsumer);
        }
    }

    public int depth() {
        return depth(root, 0) - 1;
    }

    private int depth(TreeNode<T> root, int count) {
        if (root == null) {
            return count;
        }
        return Math.max(depth(root.left, count + 1), depth(root.right, count + 1));
    }

    public String prettyPrintTree() {
        if (root == null) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(root.value);

        String pointerLeft = "├──";
        String pointerRight = (root.left != null) ? "├──" : "└──";

        prettyPrintNodes(sb, EMPTY, pointerLeft, root.right, root.left != null);
        prettyPrintNodes(sb, EMPTY, pointerRight, root.left, false);

        return sb.toString();
    }

    private void prettyPrintNodes(StringBuilder sb, String initPadding, String nodePointer, TreeNode<T> node,
                                  boolean hasLeft) {
        if (node != null) {
            sb.append(LF);
            sb.append(initPadding);
            sb.append(nodePointer);
            sb.append(node.value);

            StringBuilder paddingBuilder = new StringBuilder(initPadding);
            if (hasLeft) {
                paddingBuilder.append("│".concat(SPACE.repeat(2)));
            } else {
                paddingBuilder.append(SPACE.repeat(3));
            }
            String paddingForBoth = paddingBuilder.toString();
            String pointerLeft = "├──";
            String pointerRight = (node.left != null) ? "├──" : "└──";

            prettyPrintNodes(sb, paddingForBoth, pointerLeft, node.right, node.left != null);
            prettyPrintNodes(sb, paddingForBoth, pointerRight, node.left, false);
        }
    }

    @UtilityClass
    public static class Demo {

        public void execute() {
            BinaryTree<Integer> tree = BinaryTree.of(10, 3, 11, 6, 4, 2, 8, 1, 9, -3, 0, -4, -5, -4, -6);

            System.out.println("Constructed tree:");
            System.out.println(tree.prettyPrintTree());

            System.out.println("Size: " + tree.size());
            System.out.println("Depth: " + tree.depth());

            System.out.print("Traversal: ");
            tree.traverseAscending(System.out::println);

            System.out.printf("\nContains [%d] : %b\n", 11, tree.contains(11));
            System.out.printf("Contains [%d] : %b\n", 0, tree.contains(0));
            System.out.printf("Contains [%d] : %b\n", 5, tree.contains(5));
        }
    }
}
