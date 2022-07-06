package org.java.training.concurrency.synchronizers;

import java.util.concurrent.TimeUnit;

public class DaemonCase {
    public static void main(String[] args) {
        testDaemon();
    }

    public static void testDaemon() {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                for (int i = 0; i < 10; i++) {
                    System.out.print(i);
                }
                System.out.println("\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Finally");
            }
        });

        thread.setDaemon(true); // switch to false -> finally executed
        thread.start();

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End of main");
    }

}
