package org.java.training.concurrency.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomThreadPool {

    public static void main(String[] args) {
        testCustomThreadPool();
    }

    //TODO: improve test
    private static void testCustomThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 10, 1,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new CustomRejectionHandler());
        threadPoolExecutor.allowCoreThreadTimeOut(true);

        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is running");
                TimeUnit.MILLISECONDS.sleep(5000);
                System.out.println(Thread.currentThread().getName() + " finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < 15; i++) {
            threadPoolExecutor.execute(runnable);
        }
    }

    private static class CustomRejectionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.err.println("Executor cannot accept this task - " + r);
        }
    }
}
