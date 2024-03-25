public class Program {

    public static void main(String[] args) {
        int n = 10;
        for (int i = 0; i < n; i++) {
            Thread thread  = new Thread(new Worker(), "Thread" + (i + 1));
            thread.start();
        }
    }
}