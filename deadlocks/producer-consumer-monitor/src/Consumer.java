public class Consumer implements Runnable {
	
	private int id;
	private Buffer buffer;
	
	public Consumer(int id, Buffer buffer) {
		this.id = id;
		this.buffer = buffer;
	}
	
	private void consume(int item) {
		try {
			System.out.println("consumer " + id + ": " + item);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				var item = buffer.take();
				consume(item);
			} catch (InterruptedException e) {
			}
		}
	}

}
