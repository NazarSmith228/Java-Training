package leetcode;

public class AlternateFooBar {

    public static final Object MONITOR = new Object();

    public static void main(String[] args) {
        FooBar fooBar = new FooBar(Integer.parseInt(args[0]));

        Thread A = new Thread(
                () -> {
                    try {
                        fooBar.foo();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                , "A");

        Thread B = new Thread(
                () -> {
                    try {
                        fooBar.bar();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                , "B");

        A.start();
        B.start();
    }

    private static class FooBar {
        private final int n;
        private int current;

        public FooBar(int n) {
            this.n = n;
            this.current = 2 * n;
        }

        public void foo() throws InterruptedException {
            for (int i = 0; i < n; i++) {
                synchronized (MONITOR) {
                    if (current % 2 != 0) {
                        MONITOR.wait();
                    }
                    System.out.print("foo");
                    current--;
                    MONITOR.notify();
                }
            }
        }

        public void bar() throws InterruptedException {
            for (int i = 0; i < n; i++) {
                synchronized (MONITOR) {
                    if (current % 2 == 0) {
                        MONITOR.wait();
                    }
                    System.out.print("bar");
                    current--;
                    MONITOR.notify();
                }
            }
        }
    }
}
