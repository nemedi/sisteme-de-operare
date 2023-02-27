package problem;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;


public class ProducerConsumer {
	
	private static final int NO_ITEMS = 10; 
	private Stack<Integer> items = new Stack<Integer>();
	
	class Producer implements Runnable {
		
		public void produce(int i) {
			System.out.println("Producing " + i);
			items.push(i);
		}

		@Override
		public void run() {
			int i = 0;
			while (i++ < NO_ITEMS) {
				synchronized (items) {
					produce(i);
					items.notifyAll();
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
		}

	}
	
	class Consumer implements Runnable {
		
		private AtomicInteger consumed = new AtomicInteger();
		
		public void consume() {
			if (!items.isEmpty()) {
				System.out.println("Consuming " + items.pop());
				consumed.incrementAndGet();
			}
		}

		@Override
		public void run() {
			while (consumed.get() < NO_ITEMS) {
				synchronized (items) {
					while (items.isEmpty() && consumed.get() < NO_ITEMS) {
						try {
							items.wait(10);
						} catch (InterruptedException e) {
							Thread.interrupted();
						}
					}
					consume();
				}
			}
		}
		
	}

	public static void main(String[] args) {
		ProducerConsumer solver = new ProducerConsumer();
		Thread producerThread = new Thread(solver.new Producer());
		Consumer consumer = solver.new Consumer();
		Thread consumerThread1 = new Thread(consumer);
		Thread consumerThread2 = new Thread(consumer);
		Thread consumerThread3 = new Thread(consumer);
		producerThread.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		consumerThread1.start();
		consumerThread2.start();
		consumerThread3.start();
		try {
			consumerThread1.join();
			consumerThread2.join();
			consumerThread3.join();
		} catch (InterruptedException e) {
		}
	}

}
