class Monitor {
    private int[] buffer = new int[10];
    private int count = 0;

    public synchronized void produce() throws InterruptedException {
        int item = 0;
        while (true) {
            while (count == buffer.length) {
                wait();
            }
            buffer[count++] = item++;
            System.out.println("Produced: " + (item - 1));
            notify();
        }
    }

    public synchronized void consume() throws InterruptedException {
        while (true) {
            while (count == 0) {
                wait();
            }
            int item = buffer[--count];
            System.out.println("Consumed: " + item);
            notify();
        }
    }
}
