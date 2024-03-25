public class Program {
    public static void main(String[] args) {
        Monitor monitor = new Monitor();

        Thread producerThread = new Thread(() -> {
            try {
                monitor.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                monitor.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producerThread.start();
        consumerThread.start();
    }
}
