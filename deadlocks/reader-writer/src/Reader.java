import java.util.concurrent.Semaphore;

public class Reader implements Runnable {

	private int id;
	private Counter counter;
	private Semaphore counterLock;
	private Semaphore resourceLock;

	public Reader(int id, Counter counter, Semaphore resourceLock, Semaphore counterLock) {
		this.id = id;
		this.counter = counter;
		this.counterLock = counterLock;
		this.resourceLock = resourceLock;
	}

	@Override
	public void run() {
		while (true) {
			try {
				counterLock.acquire();
				counter.increment();
				if (counter.getValue() == 1) {
					resourceLock.acquire();
				}
				counterLock.release();
				byte[] buffer = read();
				counterLock.acquire();
				counter.decrement();
				if (counter.getValue() == 0) {
					resourceLock.release();
				}
				counterLock.release();
				System.out.println("reader" + id + ": " + new String(buffer));
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	private byte[] read() {
		return "data".getBytes();
	}

}
