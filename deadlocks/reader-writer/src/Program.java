import java.util.concurrent.Semaphore;

public class Program {

	public static void main(String[] args) {
		Counter counter = new Counter();
		Semaphore resourceLock = new Semaphore(1);
		Semaphore counterLock = new Semaphore(1);
		Writer writer = new Writer(resourceLock);
		new Thread(writer).start();
		int n = Runtime.getRuntime().availableProcessors();
		for (int i = 0; i < n; i++) {
			Reader reader = new Reader(i + 1, counter, resourceLock, counterLock);
			new Thread(reader).start();
		}
	}

}
