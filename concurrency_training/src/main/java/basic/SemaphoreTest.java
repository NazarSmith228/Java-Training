package basic;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {

    public static Semaphore semaphore = new Semaphore(0);
    public static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " started running");

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new MyRunnable());
        }

        System.out.println(threadName + " is releasing multiple semaphores");
        for (int i = 0; i < 10; i++) {
            System.out.println("Thread #" + i + " started");
            threads[i].start();
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println(threadName + " released " + i + " semaphore");
            semaphore.release();
        }

        System.out.println(threadName + " finished");
    }

    private static class MyRunnable implements Runnable {

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " is waiting for the semaphore");
            try {
                semaphore.acquire();
                System.out.println(threadName + " acquired the semaphore");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(threadName + " started incrementing");
            counter++;
            System.out.println(threadName + " finished");
        }
    }
}
