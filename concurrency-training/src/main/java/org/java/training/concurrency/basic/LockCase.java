package org.java.training.concurrency.basic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockCase {

    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public static void main(String[] args) {
        testLock();
    }

    static void testLock() {
        Runnable reader = () -> {
            readLock.lock();
            System.out.println(Thread.currentThread().getName() + " is reading");

            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + " finished reading");
                readLock.unlock();
            }
        };

        Runnable writer = () -> {
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + " is writing");

            try {
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + " finished writing");
                writeLock.unlock();
            }
        };

        Thread t1 = new Thread(reader, "T1");
        Thread t2 = new Thread(writer, "T2");
        Thread t3 = new Thread(writer, "T3");
        Thread t4 = new Thread(reader, "T4");
        Thread t5 = new Thread(reader, "T5");
        Thread t6 = new Thread(writer, "T6");
        Thread t7 = new Thread(reader, "T7");

        t2.start(); t3.start(); t6.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.start(); t4.start(); t5.start(); t7.start();
    }
}
