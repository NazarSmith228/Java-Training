package org.java.training.concurrency.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FixedThreadPool {

    public static void main(String[] args) {
        //Run the desired method
    }

    private static void testExecutors(boolean flag) {
        ExecutorService service = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 10; i++) {
            Runnable command = () -> {
                System.out.println(Thread.currentThread().getName() + " started");
//                try {
//                    TimeUnit.MILLISECONDS.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                System.out.println(Thread.currentThread().getName() + " finished");
            };
            service.execute(command);
        }
        if (flag) { // shutdown after finishing all active tasks
            service.shutdown();
            while (!service.isTerminated()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Waiting to shut down the executor");
            }
            if (service.isTerminated()) {
                System.out.println("Executor was shut down");
            }
        } else { //shutdown immediately, InterruptedException is very possible to be thrown (since threads are slipping)
            try {
                TimeUnit.NANOSECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Shut down the executor immediately");

            List<Runnable> runnables = service.shutdownNow();

            while (!service.isTerminated()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Waiting to shut down the executor");
            }
            System.out.println("Number of threads that did not finish their tasks: " + (long) runnables.size());
        }
    }

    private static void testCallableExecutor() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            Future<Integer> future = service.submit(new CallableTask(i));
            futures.add(future);
        }

        Integer result = futures.stream()
                .map(f -> {
                    Integer val = 0;
                    try {
                        val = f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return val;
                })
                .reduce(0, Integer::sum);

        service.shutdown();
        while (!service.isTerminated()) {
        }

        System.out.println("Result - " + result);
    }

    private record CallableTask(Integer num) implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " is executing");
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println(Thread.currentThread().getName() + " finished");
            return num;
        }
    }
}
