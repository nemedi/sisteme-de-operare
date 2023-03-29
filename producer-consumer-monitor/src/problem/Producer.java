package problem;

import java.util.Random;

public class Producer implements Runnable {
	
	private Buffer buffer;
	private Random random;

	public Producer(Buffer buffer) {
		this.buffer = buffer;
		this.random = new Random();
	}
	
	private int produce() {
		var item = random.nextInt(100) + 1;
		return item;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				var item = produce();
				buffer.put(item);
				System.out.println("producer: " + item);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
