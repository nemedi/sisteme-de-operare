import java.util.concurrent.Semaphore;

public class Writer implements Runnable {
	
	private Semaphore resourceLock;

	public Writer(Semaphore resourceLock) {
		this.resourceLock = resourceLock;
	}

	@Override
	public void run() {
		while (true) {
			try {
				byte[] buffer = prepare();
				resourceLock.acquire();
				write(buffer);
				resourceLock.release();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void write(byte[] buffer) {
		System.out.println("writer: " + new String(buffer));
	}

	private byte[] prepare() {
		return "data".getBytes();
	}

}
