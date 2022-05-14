package org.java.training.concurrency.leetcode;

public class PrintZeroEvenOdd {

    public static void main(String[] args) {
        ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(Integer.parseInt(args[0]));

        Thread A = new Thread(
                () -> {
                    try {
                        zeroEvenOdd.printZero();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        , "A");

        Thread B = new Thread(
                () -> {
                    try {
                        zeroEvenOdd.printEven();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        , "B");

        Thread C = new Thread(
                () -> {
                    try {
                        zeroEvenOdd.printOdd();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        , "C");

        A.start();
        B.start();
        C.start();

    }

    private static class ZeroEvenOdd {
        private final int n;
        private int curr = 1;
        private int zeroCount = 1;

        private ZeroEvenOdd(int n) {
            this.n = n;
        }

        private void printZero() throws InterruptedException {
            while (zeroCount < 2 * n) {
                synchronized (this) {
                    if (zeroCount % 2 == 0) {
                        wait();
                    }
                    System.out.print(0);
                    zeroCount++;
                    notifyAll();
                }
            }
        }

        private void printEven() throws InterruptedException {
            while (curr < n) {
                synchronized (this) {
                    if (curr % 2 == 0 || zeroCount % 2 != 0) {
                        wait();
                    }
                    System.out.print(curr);
                    curr++;
                    zeroCount++;
                    notifyAll();
                }
            }
        }

        private void printOdd() throws InterruptedException {
            while (curr < n) {
                synchronized (this) {
                    if (curr % 2 != 0 || zeroCount % 2 != 0) {
                        wait();
                    }
                    System.out.print(curr);
                    curr++;
                    zeroCount++;
                    notifyAll();
                }
            }
        }
    }
}
