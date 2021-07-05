package leetcode;

import java.util.concurrent.Semaphore;

public class TrafficLightIntersection {

    public static Semaphore regulator = new Semaphore(1);

    public static void main(String[] args) {
        TrafficLight trafficLight = new TrafficLight();

        int numberOfThreads = (int) Math.floor(Math.random() * (20 - 1 + 1) + 1);

        for (int i = 0; i < numberOfThreads; i++) {
            int curr = i + 1;
            new Thread(
                    () -> {
                        try {
                            trafficLight.carArrived(
                                    curr,
                                    (int) Math.floor(Math.random() * (2 - 1 + 1) + 1),
                                    (int) Math.floor(Math.random() * (4 - 1 + 1) + 1)
                            );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            ).start();
        }
    }


    private static class TrafficLight {

        private int roadWithGreenLight;

        public TrafficLight() {
            this.roadWithGreenLight = 1;
        }

        public void carArrived(int carId, int roadId, int direction) throws InterruptedException {
            regulator.acquire();
            if (roadId != roadWithGreenLight) {
                roadWithGreenLight = roadId;
                System.out.println("Light on road " + roadId + " turned green!");
            }
            System.out.println("Car " + carId + " on road " + roadId + " crossed the intersection in direction " + direction);
            regulator.release();
        }

    }
}
