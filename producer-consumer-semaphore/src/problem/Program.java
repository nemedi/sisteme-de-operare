package problem;

import java.util.concurrent.Semaphore;

public class Program {

	public static void main(String[] args) {
		int size = 3;
		Buffer buffer = new Buffer(size);
		Semaphore putLock = new Semaphore(size);
		Semaphore takeLock = new Semaphore(0);
		Semaphore bufferLock = new Semaphore(1);
		Producer producer = new Producer(buffer, putLock, takeLock, bufferLock);
		Consumer consumer = new Consumer(buffer, putLock, takeLock, bufferLock);
		new Thread(producer).start();
		new Thread(consumer).start();
		while (true) {
		}		
	}

}
