package org.java.training.concurrency.leetcode;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DesignBoundedBlockingQueue {

    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(20);

        Runnable enqueuer = () -> {
            try {
                blockingQueue.enqueue((int) Math.floor(Math.random() * (100 - 1 + 1) + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable dequeuer = () -> {
            try {
                blockingQueue.dequeue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        ExecutorService service = Executors.newScheduledThreadPool(20);

        for (int i = 0; i < 20; i++) {
            service.execute(enqueuer);

            if (i % 2 == 0) {
                service.execute(dequeuer);
            }
        }

        service.shutdown();

        while (!service.isTerminated()) {
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Final size of queue - " + blockingQueue.size());
    }

    private static class BlockingQueue<V> {

        private final int capacity;
        private final Queue<V> innerQueue;
        private int size;

        public BlockingQueue(int capacity) {
            this.capacity = capacity;
            this.innerQueue = new ArrayDeque<>(capacity);
            this.size = 0;
        }

        //TODO how to cancel waiting
        public synchronized void enqueue(V val) throws InterruptedException {
            long startTime = System.currentTimeMillis();

            while (isFull() && (System.currentTimeMillis() - startTime) < 10000) {
                System.out.println(Thread.currentThread().getName() + " is waiting until there is some free space");
                this.wait();
            }

            this.innerQueue.add(val);
            System.out.println("Element - " + val + " was added");
            this.size++;

            this.notifyAll();

        }

        //TODO how to cancel waiting
        public synchronized void dequeue() throws InterruptedException {
            long startTime = System.currentTimeMillis();

            while (isEmpty() && (System.currentTimeMillis() - startTime) < 10000) {
                System.out.println(Thread.currentThread().getName() + " is waiting until there are some elements added");
                this.wait();
            }

            TimeUnit.MILLISECONDS.sleep(1000);

            V removed = innerQueue.remove();
            System.out.println("Element - " + removed + " was removed");
            this.size--;

            TimeUnit.MILLISECONDS.sleep(100);

            this.notifyAll();

        }

        public synchronized V get() {
            while (isEmpty()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            V element = this.innerQueue.element();
            System.out.println("Element - " + element + " was retrieved");
            this.notifyAll();

            return element;
        }

        public int size() {
            return this.size;
        }

        public boolean isFull() {
            return this.size == this.capacity;
        }

        public boolean isEmpty() {
            return this.size == 0;
        }
    }
}
