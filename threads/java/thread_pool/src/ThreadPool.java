import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final Thread[] threads;
    private volatile boolean running;

    public ThreadPool(int poolSize) {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.threads = new Thread[poolSize];
        this.running = true;
        for (int i = 0; i < poolSize; i++) {
            threads[i] = new WorkerThread();
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

    private class WorkerThread extends Thread {
        @Override
        public void run() {
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
        }
    }

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(5);
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            threadPool.submitTask(() -> {
                System.out.println("Task " + taskId + " executed by thread: " + Thread.currentThread().getName());
            });
        }
        threadPool.shutdown();
    }
}
