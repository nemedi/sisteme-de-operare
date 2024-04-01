import java.util.Random;

public class Producer implements Runnable {
	
	private int id;
	private Buffer buffer;
	private Random random;

	public Producer(int id, Buffer buffer) {
		this.id = id;
		this.buffer = buffer;
		this.random = new Random();
	}
	
	private int produce() {
		var item = random.nextInt(100) + 1;
		System.out.println("producer " + id + " : " + item);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		return item;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				var item = produce();
				buffer.put(item);
			} catch (InterruptedException e) {
			}
		}
	}

}
