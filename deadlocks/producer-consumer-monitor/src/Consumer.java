public class Consumer implements Runnable {
	
	private Buffer buffer;
	
	public Consumer(Buffer buffer) {
		this.buffer = buffer;
	}
	
	private void consume(int item) {
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				var item = buffer.take();
				System.out.println("consumer: " + item);
				consume(item);
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
