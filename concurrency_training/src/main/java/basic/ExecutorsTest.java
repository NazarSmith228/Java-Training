package basic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorsTest {

    public static void main(String[] args) {
        //Run the desired method
    }

    static void testExecutors(boolean flag) {
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

    static AtomicInteger counter = new AtomicInteger(0);

    static void testCachedExecutors() {
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

    static void testCallableExecutor() {
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

    //TODO improve test
    static void testCustomThreadPool() {
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

    static class CustomRejectionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.err.println("Executor cannot accept this task - " + r);
        }
    }
}
