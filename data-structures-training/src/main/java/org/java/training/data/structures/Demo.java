package org.java.training.data.structures;

import org.java.training.data.structures.impl.ArrayFormatter;
import org.java.training.data.structures.impl.BinaryTree;
import org.java.training.data.structures.impl.HashTable;

public class Demo {

    public static void main(String[] args) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        hashTableDemo();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        arrayFormatterDemo();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        binaryTreeDemo();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
    }

    private static void hashTableDemo() {
        var table = new HashTable<String>(5) {{
            add("AaAaAa");
            add("AaAaBB");
            add("AaAaAa");
            add("AaBBAa");
            add("AaBBBB");
            add("AAa");
            add("bBB");
            add("bbb");
            add("BBb");
            add("aaAA");
            add("cCCc");
        }};

        table.content();
    }

    private static void arrayFormatterDemo() {
        String[] input = {"1", "2", "3", "x", "5", "6", "a", "porosiatko", "c",
                "fdsdfsdfsdfs", "sdfsdq", "12", "13", "14", "15", "16"};
        ArrayFormatter.printFormatted(input, 4);
    }

    private static void binaryTreeDemo() {
        var tree = new BinaryTree<Integer>() {{
            insert(10);
            insert(3);
            insert(11);
            insert(6);
            insert(4);
            insert(2);
            insert(7);
            insert(8);
            insert(1);
            insert(9);
            insert(-3);
            insert(0);
            insert(-4);
            insert(-5);
            insert(-4);
            insert(-6);
        }};

        System.out.println("Constructed tree:");
        System.out.println(tree.prettyPrintTree());

        System.out.println("Size: " + tree.size());
        System.out.println("Depth: " + tree.depth());

        System.out.print("Traversal: ");
        tree.traverseAscending();

        System.out.printf("\nContains %d : %b\n", 11, tree.contains(11));
        System.out.printf("Contains %d : %b\n", 0, tree.contains(0));
        System.out.printf("Contains %d : %b\n", 5, tree.contains(5));
    }
}
