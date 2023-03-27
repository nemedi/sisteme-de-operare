package problem;

import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Consumer implements Runnable {
	
	private Queue<Integer> buffer;
	private Semaphore empty;
	private Semaphore full;
	private Semaphore mutex;

	public Consumer(Queue<Integer> buffer, Semaphore empty, Semaphore full, Semaphore mutex) {
		this.buffer = buffer;
		this.empty = empty;
		this.full = full;
		this.mutex = mutex;
	}

	@Override
	public void run() {
		while (true) {
			try {
				full.acquire();
				mutex.acquire();
				int value = buffer.poll();
				consume(value);
				mutex.release();
				empty.release();
				
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void consume(int value) throws InterruptedException {
		System.out.println("Consumer consumed " + value);
		Thread.sleep(500);
	}

}
