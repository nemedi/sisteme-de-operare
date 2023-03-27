package problem;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Test {
	
	private static final int CAPACITY;
	private static final int WORKERS;
	private static final long INTERVAL;
	private static final long TIMEOUT;
	private static final boolean DEBUG;
	private static final AtomicInteger SEQUENCE = new AtomicInteger();
	private static final Supplier<Integer> SUPPLIER;
	private static final Consumer<Integer> CONSUMER;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("settings");
		CAPACITY = Integer.parseInt(bundle.getString("capacity"));
		WORKERS = Integer.parseInt(bundle.getString("workers"));
		INTERVAL = Long.parseLong(bundle.getString("interval"));
		TIMEOUT = Long.parseLong(bundle.getString("timeout"));
		DEBUG = Boolean.parseBoolean(bundle.getString("debug"));
		SUPPLIER = () -> {
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
			}
			return SEQUENCE.incrementAndGet();
		};
		CONSUMER = System.out::println;
	}
	
	public static void main(String[] args) {
		try {
			if (args.length == 1) {
				Test.class.getDeclaredMethod(args[0]).invoke(null);
			}
		} catch (Exception e) {
			if (DEBUG) {
				e.printStackTrace();
			}
		}
	}
	
	private static Storage<Integer> begin() {
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		System.out.println("==================================================");
		System.out.println("Begin running method " + methodName);
		System.out.println("--------------------------------------------------");
		SEQUENCE.set(0);
		return new Storage<Integer>(CAPACITY, DEBUG);
	}
	
	private static void run(List<ProducerWorker<Integer>> producers,
			List<ConsumerWorker<Integer>> consumers) throws InterruptedException {
		final List<Thread> threads = new ArrayList<Thread>();
		final AtomicInteger index = new AtomicInteger();
		index.set(0);
		producers.forEach(worker ->
			threads.add(new Thread(worker, "Producer" + index.incrementAndGet())));
		index.set(0);
		consumers.forEach(worker ->
			threads.add(new Thread(worker, "Consumer" + index.incrementAndGet())));
		threads.forEach(thread -> thread.start());
		Thread.sleep(TIMEOUT);
	}
	
	private static <T> void end(Storage<T> storage) throws InterruptedException {
		Thread.sleep(TIMEOUT);
		storage.stop();
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		System.out.println("--------------------------------------------------");
		System.out.println("End running method " + methodName);
		System.out.println("==================================================");
		System.exit(0);
	}
	
	private static List<ProducerWorker<Integer>> createProducers(Storage<Integer> storage, int size) {
		return IntStream.range(0, size)
				.mapToObj(index -> new ProducerWorker<Integer>(storage, SUPPLIER, DEBUG))
				.collect(toList());
	}
	
	private static List<ConsumerWorker<Integer>> createConsumers(Storage<Integer> storage, int size) {
		return IntStream.range(0, size)
				.mapToObj(index -> new ConsumerWorker<Integer>(storage, CONSUMER, DEBUG))
				.collect(toList());
	}
	
	private static void singleProducerAndSingleConsumer() throws InterruptedException {
		Storage<Integer> storage = begin();
		run(createProducers(storage, 1), createConsumers(storage, 1));
		end(storage);
	}
	
	public static void singleProducerAndMultipleConsumers() throws InterruptedException {
		Storage<Integer> storage = begin();
		run(createProducers(storage, 1), createConsumers(storage, WORKERS));
		end(storage);
	}

	public static void multipleProducersAndSingleConsumer() throws InterruptedException {
		Storage<Integer> storage = begin();
		run(createProducers(storage, WORKERS), createConsumers(storage, 1));
		end(storage);
	}
	
	public static void multipleProducersAndMultipleConsumers() throws InterruptedException {
		Storage<Integer> storage = begin();
		run(createProducers(storage, WORKERS), createConsumers(storage, WORKERS));
		end(storage);
	}
}
