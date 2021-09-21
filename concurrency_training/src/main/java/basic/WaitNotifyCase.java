package basic;

import java.util.concurrent.TimeUnit;

public class WaitNotifyCase {

    public static int counter;
    public static final Object monitor = new Object();

    public static void main(String[] args) {
        testSynchronization();
    }

    static void testSynchronization() {
        Thread t1 = new Thread(() -> {
            synchronized (monitor) {
                System.out.println(Thread.currentThread().getName() + " started incrementing");

                for (int i = 0; i < 5; i++) {
                    counter++;
                }

                System.out.println(Thread.currentThread().getName() + " finished incrementing");
                monitor.notify();
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            synchronized (monitor) {

                while (counter < 5) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(Thread.currentThread().getName() + " started incrementing");

                for (int i = 0; i < 5; i++) {
                    counter++;
                }

                System.out.println(Thread.currentThread().getName() + " finished incrementing");
                monitor.notify();
            }
        }, "T2");

        t2.start();

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.start();

        synchronized (monitor) {

            while (counter < 10) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Main thread started incrementing");

            for (int i = 0; i < 5; i++) {
                counter++;
            }

            System.out.println("Main thread finished incrementing");
        }
        System.out.println("Counter equals 15? - " + (counter == 15) + " - " + counter);
    }

}
