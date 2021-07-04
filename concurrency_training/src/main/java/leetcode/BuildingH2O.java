package leetcode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class BuildingH2O {
    public static Semaphore hSemaphore = new Semaphore(2);
    public static Semaphore oSemaphore = new Semaphore(0);

    public static void main(String[] args) {
        String formula = args[0];
        int oxygenThreads = formula.length() / 3;
        int hydrogenThreads = formula.length() - oxygenThreads;

        H2O h2O = new H2O();

        ExecutorService hydrogenService = Executors.newFixedThreadPool(hydrogenThreads);
        ExecutorService oxygenService = Executors.newFixedThreadPool(oxygenThreads);
        for (int i = 0; i < hydrogenThreads; i++) {
            Thread t = new Thread(
                    () -> {
                        try {
                            h2O.hydrogen();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
            hydrogenService.execute(t);
        }

        for (int i = 0; i < oxygenThreads; i++) {
            Thread t = new Thread(
                    () -> {
                        try {
                            h2O.oxygen();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
            oxygenService.execute(t);
        }

        hydrogenService.shutdown();
        oxygenService.shutdown();
    }

    private static class H2O {
        private int hydrogenCount = 0;

        public H2O() {

        }

        public void hydrogen() throws InterruptedException {
            hSemaphore.acquire();
            System.out.print("H");
            hydrogenCount++;
            if (hydrogenCount % 2 == 0) {
                hydrogenCount = 0;
                oSemaphore.release();
            }
        }

        public void oxygen() throws InterruptedException {
            oSemaphore.acquire();
            System.out.print("O");
            hSemaphore.release();
            hSemaphore.release();
        }
    }
}
