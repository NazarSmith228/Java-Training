package org.java.training.generics;

import org.java.training.generics.impl.HeterogeneousMaxHolder;

public class Demo {

    public static void main(String[] args) {
        maxHolderDemo();
    }

    private static void maxHolderDemo() {
        var maxHolder = new HeterogeneousMaxHolder();

        maxHolder.put(Integer.class, 3);
        maxHolder.put(Integer.class, 1);
        maxHolder.put(Integer.class, 2);
        System.out.println(maxHolder.getMax(Integer.class)); //3

        maxHolder.put(String.class, "A");
        maxHolder.put(String.class, "c");
        maxHolder.put(String.class, "a");
        System.out.println(maxHolder.getMax(String.class)); // c
    }
}
