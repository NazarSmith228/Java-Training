package org.java.training.concurrency.leetcode;

public class FizzBuzzMultithreaded {

    private static final Object monitor = new Object();

    public static void main(String[] args) {
        FizzBuzz fizzBuzz = new FizzBuzz(Integer.parseInt(args[0]));

        Thread A = new Thread(
                () -> {
                    try {
                        fizzBuzz.fizz();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                , "A");

        Thread B = new Thread(
                () -> {
                    try {
                        fizzBuzz.buzz();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                , "B");

        Thread C = new Thread(
                () -> {
                    try {
                        fizzBuzz.fizzBuzz();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                , "C");

        Thread D = new Thread(
                () -> {
                    try {
                        fizzBuzz.number();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                , "D");

        A.start();
        B.start();
        C.start();
        D.start();
    }

    private static class FizzBuzz {
        private final int n;
        private int current = 1;

        public FizzBuzz(int n) {
            this.n = n;
        }

        public void fizz() throws InterruptedException {
            while (current <= n) {
                synchronized (monitor) {
                    if (current % 3 == 0 && current % 5 != 0) {
                        System.out.print("fizz ");
                        current++;
                        monitor.notifyAll();
                    } else {
                        monitor.wait();
                    }
                }
            }
        }

        public void buzz() throws InterruptedException {
            while (current <= n) {
                synchronized (monitor) {
                    if (current % 3 != 0 && current % 5 == 0) {
                        System.out.print("buzz ");
                        current++;
                        monitor.notifyAll();
                    } else {
                        monitor.wait();
                    }
                }
            }
        }

        public void fizzBuzz() throws InterruptedException {
            while (current <= n) {
                synchronized (monitor) {
                    if (current % 3 == 0 && current % 5 == 0) {
                        System.out.print("fizzbuzz ");
                        current++;
                        monitor.notifyAll();
                    } else {
                        monitor.wait();
                    }
                }
            }
        }

        public void number() throws InterruptedException {
            while (current <= n) {
                synchronized (monitor) {
                    if (current % 3 != 0 && current % 5 != 0) {
                        System.out.print(current + " ");
                        current++;
                        monitor.notifyAll();
                    } else {
                        monitor.wait();
                    }
                }
            }
        }
    }
}
