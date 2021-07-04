package basic;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    static int counter = 0;

    public static void main(String[] args) {
        testLatch();
    }

    @SuppressWarnings("duplicates")
    static void testLatch() {
        CountDownLatch first = new CountDownLatch(1);
        CountDownLatch second = new CountDownLatch(1);
        CountDownLatch third = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
            try {
                first.await();
                System.out.println(Thread.currentThread().getName() + " started");

                for (int i = 0; i < 5; i++) {
                    counter++;
                }

                System.out.println("Counter after " + Thread.currentThread().getName() + " " + counter);
                second.countDown();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished");
        }, "T1");

        Thread t2 = new Thread(() -> {
            try {
                second.await();
                System.out.println(Thread.currentThread().getName() + " started");

                for (int i = 0; i < 5; i++) {
                    counter++;
                }

                System.out.println("Counter after " + Thread.currentThread().getName() + " " + counter);
                third.countDown();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished");
        }, "T2");

        t1.start();
        t2.start();

        System.out.println(Thread.currentThread().getName() + " started");

        for (int i = 0; i < 5; i++) {
            counter++;
        }
        System.out.println("Counter after " + Thread.currentThread().getName() + " " + counter);

        first.countDown();
        System.out.println(Thread.currentThread().getName() + " began waiting");

        try {
            third.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " finished waiting");
        System.out.println("Counter - " + counter);
    }
}
