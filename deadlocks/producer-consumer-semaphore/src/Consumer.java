import java.util.Random;
import java.util.concurrent.Semaphore;

public class Consumer implements Runnable {

	private int id;
	private Buffer buffer;
	private Semaphore putLock;
	private Semaphore takeLock;
	private Semaphore bufferLock;

	public Consumer(int id,
			Buffer buffer,
			Semaphore putLock,
			Semaphore takeLock,
			Semaphore bufferLock) {
		this.id = id;
		this.buffer = buffer;
		this.putLock = putLock;
		this.takeLock = takeLock;
		this.bufferLock = bufferLock;
	}

	@Override
	public void run() {
		while (true) {
			try {
				takeLock.acquire();
				bufferLock.acquire();
				int item = buffer.take();
				bufferLock.release();
				putLock.release();
				consume(item);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void consume(int item) {
		System.out.println("consumer " + id + ": " + item);
		Delayer.delay();
	}
}
