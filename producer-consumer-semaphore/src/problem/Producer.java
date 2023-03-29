package problem;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Producer implements Runnable {

	private Buffer buffer;
	private Semaphore putLock;
	private Semaphore takeLock;
	private Semaphore bufferLock;
	private Random random;

	public Producer(Buffer buffer,
			Semaphore putLock,
			Semaphore takeLock,
			Semaphore bufferLock) {
		this.buffer = buffer;
		this.putLock = putLock;
		this.takeLock = takeLock;
		this.bufferLock = bufferLock;
		this.random = new Random();
	}

	@Override
	public void run() {
		
		while (true) {
			try {
				int item = produce();
				putLock.acquire();
				bufferLock.acquire();
				buffer.put(item);
				System.out.println("producer: " + item);
				bufferLock.release();
				takeLock.release();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private int produce() {
		return random.nextInt(100) + 1;
	}

}
