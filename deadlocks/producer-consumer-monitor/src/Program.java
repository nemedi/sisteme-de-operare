public class Program {

	public static void main(String[] args) {
		int size = Runtime.getRuntime().availableProcessors();
		final Buffer buffer = new Buffer(size);
		final Producer producer = new Producer(buffer);
		final Consumer consumer = new Consumer(buffer);
		new Thread(producer).start();
		new Thread(consumer).start();
	}

}
