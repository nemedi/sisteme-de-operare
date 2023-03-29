package problem;

import java.util.concurrent.Semaphore;

public class Consumer implements Runnable {
	
	private Buffer buffer;
	private Semaphore putLock;
	private Semaphore takeLock;
	private Semaphore bufferLock;

	public Consumer(Buffer buffer,
			Semaphore full,
			Semaphore empty,
			Semaphore mutex) {
		this.buffer = buffer;
		this.putLock = full;
		this.takeLock = empty;
		this.bufferLock = mutex;
	}

	@Override
	public void run() {
		while (true) {
			try {
				takeLock.acquire();
				bufferLock.acquire();
				int item = buffer.take();
				bufferLock.release();
				if (buffer.isEmpty()) { 
					putLock.release(buffer.getSize());
				}
				consume(item);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void consume(int item) {
	}

}
