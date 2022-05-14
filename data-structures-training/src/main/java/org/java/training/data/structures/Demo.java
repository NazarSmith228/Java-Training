package org.java.training.data.structures;

import org.java.training.data.structures.impl.HashTable;

public class Demo {

    public static void main(String[] args) {
        hashTableDemo();
    }

    private static void hashTableDemo() {
        var table = new HashTable<String>(5);
        //same hashcode -> index 0
        table.add("AaAaAa");
        table.add("AaAaBB");
        table.add("AaBBAa");
        table.add("AaBBBB");

        //same hashcode -> index 1
        table.add("AAa");
        table.add("bBB");
        table.add("bbb");

        //index 3
        table.add("BBb");
        //index 5
        table.add("aaAA");
        //index 6
        table.add("cCCc");

        table.content();
    }
}
