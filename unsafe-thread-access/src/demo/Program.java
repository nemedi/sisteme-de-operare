package demo;

import java.util.Arrays;
import java.util.List;

public class Program {

	public static void main(String[] args) {
		int n = Runtime.getRuntime().availableProcessors();
		List<Integer> list = null;
		list = singleThreadedProcessing(n);
		System.out.println("Single-threaded");
		printList(list, false);
		list = multiThreadedProcessing(n, false);
		System.out.println("Multi-threaded without lock");
		printList(list, true);
		list = multiThreadedProcessing(n, true);
		System.out.println("Multi-threaded with lock");
		printList(list, true);
	}
	
	private static void printList(List<Integer> list, boolean sort) {
		Integer[] array = list.toArray(new Integer[list.size()]);
		if (sort) {
			Arrays.sort(array);
		}
		Arrays.stream(array).forEach(System.out::println);
		list.clear();
	}
	
	private static List<Integer> multiThreadedProcessing(int n, boolean lock) {
		Thread[] threads = new Thread[n];
		for (int i = 0; i < threads.length; i++) {
			Worker worker = new Worker(i + 1, lock);
			threads[i] = new Thread(worker);
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
			}
		}
		return Worker.getList();
	}
	
	private static List<Integer> singleThreadedProcessing(int n) {
		for (int i = 0; i < n; i++) {
			Worker worker = new Worker(i + 1, false);
			worker.run();
		}
		return Worker.getList();
	}

}
