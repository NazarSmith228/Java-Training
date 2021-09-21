package basic;

public class JoinCase {

    public static void main(String[] args) {
        testJoin();
    }

    public static void testJoin() {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.print(i);
            }
            System.out.println("\n" + Thread.currentThread().getName() + " finished");
        }, "T2");

        thread.start();

        try {
            System.out.println("Before joining T2");
//            TimeUnit.MILLISECONDS.sleep(1);
            thread.join();// main thread waits until created thread terminates
            System.out.println("After joining T2");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("End of main");
    }

}
