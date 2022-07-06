package org.java.training.concurrency.executors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPool {

    public static void main(String[] args) {
        testScheduledExecutors();
    }

    static void testScheduledExecutors() {
        final CountDownLatch latch = new CountDownLatch(10);

        final ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        System.out.println("Default scheduler started - " + dtf.format(LocalDateTime.now()));

        for (int i = 0; i < 10; i++) {
            service.schedule(new ScheduledTask(latch), 3, TimeUnit.SECONDS);
        }
//        service.scheduleWithFixedDelay(new ScheduledTask(latch), 3, 5, TimeUnit.SECONDS); //execute with fixed delay
        try {
            System.out.println(Thread.currentThread().getName() + " is waiting for the executor to finish.");
            latch.await();
            System.out.println("Default scheduler finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.shutdown();
        System.out.println(Thread.currentThread().getName() + " finished");
    }

    private static class ScheduledTask implements Runnable {
        private final CountDownLatch latch;

        private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        public ScheduledTask(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " started: " + dtf.format(LocalDateTime.now()));
                TimeUnit.MILLISECONDS.sleep(500);
                latch.countDown();
                System.out.println(Thread.currentThread().getName() + " finished: " + dtf.format(LocalDateTime.now()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
