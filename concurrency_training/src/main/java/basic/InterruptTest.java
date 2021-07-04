package basic;

public class InterruptTest {

    public static void main(String[] args) {
        testInterrupt();
    }

    static void testInterrupt() {
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " started");
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName() + " finished");
            } catch (InterruptedException e) {
                System.err.println(Thread.currentThread().getName() + " was interrupted");
                throw new RuntimeException(e);
            }
        };
        System.out.println(Thread.currentThread().getName() + " started");

        Thread t1 = new Thread(runnable, "T1");
        Thread t2 = new Thread(runnable, "T2");

        t2.setUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println(thread.getName() + " threw " + throwable.getMessage());
        });

        //TODO how to catch the exception in parent thread?
        try {
            t1.start();
            t2.start();
            System.out.println(Thread.currentThread().getName() + " interrupting " + t2.getName());
            t2.interrupt();
        } catch (RuntimeException e) { // useless....
            System.err.println(t2.getName() + " was successfully interrupted from " + Thread.currentThread().getName());
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " finished");
    }
}
