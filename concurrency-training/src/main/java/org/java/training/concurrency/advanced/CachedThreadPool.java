package org.java.training.concurrency.advanced;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CachedThreadPool {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        testCachedExecutors();
    }

    private static void testCachedExecutors() {
        ExecutorService service = Executors.newCachedThreadPool();

        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + " incrementing");
            for (int i = 0; i < 5; i++) {
                counter.incrementAndGet();
            }
        };

        long begin = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            service.execute(runnable);
        }
        service.shutdown();
        while (!service.isTerminated()) {
        }
        long end = System.currentTimeMillis();

        System.out.printf("Service is terminated. Final counter - %d. Time taken - %d milliseconds.",
                counter.get(), end - begin);
    }

}
