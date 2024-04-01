import java.util.Random;
import java.util.concurrent.Semaphore;

public class Producer implements Runnable {

	private int id;
	private Buffer buffer;
	private Semaphore putLock;
	private Semaphore takeLock;
	private Semaphore bufferLock;
	private Random random;

	public Producer(int id,
			Buffer buffer,
			Semaphore putLock,
			Semaphore takeLock,
			Semaphore bufferLock) {
		this.id = id;
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
				bufferLock.release();
				takeLock.release();
			} catch (InterruptedException e) {
			}
		}
	}

	private int produce() {
		int item = random.nextInt(100) + 1;
		System.out.println("producer " + id + ": " + item);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		return item;
	}
}
