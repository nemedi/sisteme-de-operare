import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Program {

	public static void main(String[] args) {
		int n = 5;
		int size = 10;
		Buffer buffer = new Buffer(size);
		Semaphore producerLock = new Semaphore(size);
		Semaphore consumerLock = new Semaphore(0);
		Semaphore bufferLock = new Semaphore(1);
		ExecutorService executorService = Executors.newFixedThreadPool(2 * n);
		for (int i = 0; i < n; i++) {
			Producer producer = new Producer(i + 1, buffer,
				producerLock, consumerLock, bufferLock);
			Consumer consumer = new Consumer(i + 1, buffer,
				producerLock, consumerLock, bufferLock);
			executorService.submit(producer);
			executorService.submit(consumer);
		}
		executorService.shutdown();
	}
}
