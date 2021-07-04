package leetcode;

public class PrintInOrder {
    public static int counter = 3;
    public static final Object monitor = new Object();

    public static void main(String[] args) {
        Printer printer = new Printer();
        Thread A = new Thread(
                () -> printer.print(Integer.parseInt(args[0]))
        );

        Thread B = new Thread(
                () -> printer.print(Integer.parseInt(args[1]))
        );

        Thread C = new Thread(
                () -> printer.print(Integer.parseInt(args[2]))
        );

        A.start();
        B.start();
        C.start();

    }

    private static class Printer {

        public void print(int num) {
            switch (num) {
                case 1 -> printFirst();
                case 2 -> printSecond();
                case 3 -> printThird();
                default -> throw new IllegalArgumentException();
            }
        }

        private void printFirst() {
            synchronized (monitor) {
                System.out.println("First");
                counter--;
                monitor.notifyAll();
            }
        }

        private void printSecond() {
            synchronized (monitor) {
                while (counter > 2) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Second");
                counter--;
                monitor.notifyAll();
            }
        }

        private void printThird() {
            synchronized (monitor) {
                while (counter > 1) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Third");
                counter--;
                monitor.notifyAll();
            }
        }

    }
}
