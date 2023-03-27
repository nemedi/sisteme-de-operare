package problem;

import java.util.function.Consumer;

public class ConsumerWorker<T> implements Runnable {
	
	private Storage<T> storage;
	private Consumer<T> consumer;
	private boolean debug;
	
	public ConsumerWorker(Storage<T> storage, Consumer<T> consumer, boolean debug) {
		this.storage = storage;
		this.consumer = consumer;
		this.debug = debug;
	}
	
	public ConsumerWorker(Storage<T> storage, Consumer<T> consumer) {
		this(storage, consumer, false);
	}

	@Override
	public void run() {
		while (storage.isRunning()) {
			try {
				storage.waitWhenIsEmpty();
				T data = storage.take();
				if (debug) {
					System.out.println(Thread.currentThread().getName() + " consumed " + data);
				}
				storage.notifyNotFull();
				consumer.accept(data);
			} catch (InterruptedException e) {
				if (debug) {
					e.printStackTrace();
				}
				break;
			}
			
		}
	}

}
