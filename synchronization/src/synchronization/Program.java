package synchronization;

import java.util.Random;
import java.util.function.BiFunction;

public class Program {
	
	private static final Random random = new Random();
	
	private static void run(BiFunction<Integer, Integer, Boolean> function, int k, int n) {
		long begin = System.currentTimeMillis();
		function.apply(k, n);
		long end = System.currentTimeMillis();
		System.out.println(Long.toString(end - begin) + "ms");
	}
	
	private static void doSomething() {
		try {
			System.out.print(Thread.currentThread().getName() + " ");
			Thread.sleep(random.nextInt(500));
		} catch (InterruptedException e) {
		}
	}
	
	private static boolean singleThreaded(int k, int n) {
		for (int i = 0; i < k * n; i++) {
			Thread.currentThread().setName(Integer.toString(i % k + 1));
			doSomething();
		}
		return true;
	}
	
	private static boolean unorderedMultiThreaded(int k, int n) {
		Runnable runnable = () -> {
			for (int i = 0; i < n; i++) {
				doSomething();
			}
		};
		Thread[] threads = new Thread[k];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(runnable);
			threads[i].setName(Integer.toString(i + 1));
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		try {
			for (int i = 0; i < threads.length; i++) {
				threads[i].join();
			}
		} catch (InterruptedException e) {
		}
		return true;
	}
	
	private static boolean orderedMultiThreaded(int k, int n) {

		class Worker implements Runnable {
			
			private Object currentLock;
			private Object nextLock;

			Worker(Object currentLock, Object nextLock) {
				this.currentLock = currentLock;
				this.nextLock = nextLock;
			}

			@Override
			public void run() {
				try {
					for (int i = 0; i < n; i++) {
						synchronized (currentLock) {
							currentLock.wait();
						}
						doSomething();
						synchronized (nextLock) {
							nextLock.notify();
						}
					}
				} catch (InterruptedException e) {
				}
			}
			
		};
		
		Object[] locks = new Object[k];
		for (int i = 0; i < locks.length; i++) {
			locks[i] = new Object();
		}
		Thread[] threads = new Thread[k];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Worker(locks[i % k], locks[(i + 1) % k]));
			threads[i].setName(Integer.toString(i + 1));
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		synchronized (locks[0]) {
			locks[0].notify();
		}
		try {
			for (int i = 0; i < threads.length; i++) {
				threads[i].join();
			}
		} catch (InterruptedException e) {
		}
		return true;
	}
	
	public static void main(String[] args) {
		int k = Runtime.getRuntime().availableProcessors();
		run(Program::singleThreaded, k, 10);
		run(Program::unorderedMultiThreaded, k, 10);
		run(Program::orderedMultiThreaded, k, 10);
	}
	
}