public class Program {

    public static void main(String[] args) throws InterruptedException {
        String threadName = "MyThread";
        Thread thread = new Thread(() ->
            System.out.println("Hello World from " + Thread.currentThread().getName() + "!"));
        thread.setName(threadName);
        thread.start();
        thread.join();
    }
}