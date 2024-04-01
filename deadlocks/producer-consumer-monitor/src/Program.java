import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Program {

	public static void main(String[] args) {
		int n = 5;
		int size = 10;
		final Buffer buffer = new Buffer(size);
		ExecutorService executorService = Executors.newFixedThreadPool(2 * n);
		for (int i = 0; i < n; i++) {
			Producer producer = new Producer(i + 1, buffer);
			Consumer consumer = new Consumer(i + 1, buffer);
			executorService.submit(producer);
			executorService.submit(consumer);
		}
		executorService.shutdown();
	}

}
