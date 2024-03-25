public class Worker extends Thread {

    private Singleton singleton;

    @Override
    public void run() {
        singleton = Singleton.getInstance();
    }

    public Singleton getSingleton() {
        return singleton;
    }
    
}
