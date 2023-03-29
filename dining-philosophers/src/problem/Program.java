package problem;

import java.util.concurrent.Semaphore;

public class Program {

	public static void main(String[] args) {
		final int n = Runtime.getRuntime().availableProcessors();
		Thread[] threads = new Thread[n];
		Semaphore[] forks = new Semaphore[n];
		for (int i = 0; i < forks.length; i++) {
			forks[i] = new Semaphore(1);
		}
		for (int i = 0; i < threads.length; i++) {
			var philosopher = new Philosopher(i + 1, forks[i],
					forks[(i + 1) % forks.length]);
			threads[i] = new Thread(philosopher);
			threads[i].start();
		}
	}

}
