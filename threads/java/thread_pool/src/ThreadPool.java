import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final Thread[] threads;
    private volatile boolean running;

    public ThreadPool(int poolSize) {
        this.taskQueue = new LinkedBlockingQueue<Runnable>();
        this.threads = new Thread[poolSize];
        this.running = true;
		final Runnable runnable = () -> {
			while (running || !taskQueue.isEmpty()) {
                try {
                	Runnable task = taskQueue.take();
                	if (task != null) {
	                    task.run();
                	}
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
		};
        for (int i = 0; i < poolSize; i++) {
            threads[i] = new Thread(runnable);
			threads[i].setName("Thread-" + (i + 1));
            threads[i].start();
        }
    }

    public void submitTask(Runnable task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void shutdown() {
    	this.running = false;
    }
}
