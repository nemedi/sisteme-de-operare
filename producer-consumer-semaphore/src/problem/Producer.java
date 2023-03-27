package problem;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Producer implements Runnable {
	
	private Queue<Integer> buffer;
	private Semaphore empty;
	private Semaphore full;
	private Semaphore mutex;
	private Random random;

	public Producer(Queue<Integer> buffer, Semaphore empty, Semaphore full, Semaphore mutex) {
		this.buffer = buffer;
		this.empty = empty;
		this.full = full;
		this.mutex = mutex;
		this.random = new Random();
	}

	@Override
	public void run() {
		
		while (true) {
			try {
				int value = produce();
				empty.acquire();
				mutex.acquire();
				buffer.add(value);
				mutex.release();
				full.release();
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private int produce() throws InterruptedException {
		Thread.sleep(500);
		int value = random.nextInt(100);
		System.out.println("Producer produced " + value);
		return value;
	}

}
