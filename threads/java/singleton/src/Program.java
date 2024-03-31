public class Program {

    public static void main(String[] args) throws InterruptedException {
        int n = 10;
        Worker[] workers = new Worker[n];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
        for (int i = 0; i < workers.length; i++) {
            workers[i].join();
        }
        for (int i = 0; i < workers.length - 1; i++) {
            for (int j = i + 1; j < workers.length; j++) {
                if (workers[i].getSingleton() != workers[j].getSingleton()) {
                    System.err.println("Multiple instance of the Singleton class were created.");
                    System.exit(1);
                }
            }
        }
        System.out.println("Only one instance of the Singleton class was created.");
        System.exit(0);
    }
}
