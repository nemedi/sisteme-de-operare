import java.util.concurrent.Semaphore;

public class Consumer implements Runnable {

	private int id;
	private Buffer buffer;
	private Semaphore putLock;
	private Semaphore takeLock;
	private Semaphore bufferLock;

	public Consumer(int id,
			Buffer buffer,
			Semaphore full,
			Semaphore empty,
			Semaphore mutex) {
		this.id = id;
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
				putLock.release();
				consume(item);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void consume(int item) {
		System.out.println("consumer " + id + ": " + item);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
}
