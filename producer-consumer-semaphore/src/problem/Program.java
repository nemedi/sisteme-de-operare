package problem;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Program {

	public static void main(String[] args) {
		Queue<Integer> buffer = new LinkedList<Integer>();
		int capacity = 10;
		Semaphore empty = new Semaphore(capacity);
		Semaphore full = new Semaphore(0);
		Semaphore mutex = new Semaphore(1);
		Thread producer = new Thread(new Producer(buffer, empty, full, mutex));
		Thread consumer = new Thread(new Consumer(buffer, empty, full, mutex));
		producer.start();
		consumer.start();
		while (true) {
		}
	}
}
