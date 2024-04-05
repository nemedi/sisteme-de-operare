public class Consumer implements Runnable {
	
	private int id;
	private Buffer buffer;
	
	public Consumer(int id, Buffer buffer) {
		this.id = id;
		this.buffer = buffer;
	}
	
	private void consume(int item) {
		System.out.println("consumer " + id + ": " + item);
		Delayer.delay();
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
