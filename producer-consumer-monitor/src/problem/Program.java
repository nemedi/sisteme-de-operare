package problem;

public class Program {

	public static void main(String[] args) {
		int size = Runtime.getRuntime().availableProcessors();
		Buffer buffer = new Buffer(size);
		Producer producer = new Producer(buffer);
		Consumer consumer = new Consumer(buffer);
		new Thread(producer).start();
		new Thread(consumer).start();
	}

}
