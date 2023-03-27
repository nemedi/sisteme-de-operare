package problem;

import java.util.function.Supplier;

public class ProducerWorker<T> implements Runnable {
	
	private Storage<T> storage;
	private Supplier<T> supplier;
	private boolean debug;

	public ProducerWorker(Storage<T> storage, Supplier<T> supplier, boolean debug) {
		this.storage = storage;
		this.supplier = supplier;
		this.debug = debug;
	}
	
	public ProducerWorker(Storage<T> storage, Supplier<T> supplier) {
		this(storage, supplier, false);
	}

	@Override
	public void run() {
		while (storage.isRunning()) {
			try {
				storage.waitWhenIsFull();
				T data = supplier.get();
				storage.put(data);
				if (debug) {
					System.out.println(Thread.currentThread().getName() + " produced " + data);
				}
				storage.notifyAllIsNoLongerEmpty();
			} catch (InterruptedException e) {
				if (debug) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

}
