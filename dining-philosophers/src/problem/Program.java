package problem;

import java.util.concurrent.Semaphore;

public class Program {

	public static void main(String[] args) {
		Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];
		Semaphore[] semaphores = new Semaphore[threads.length];
		for (int i = 0; i < semaphores.length; i++) {
			semaphores[i] = new Semaphore(1);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Philosopher(i, semaphores));
			threads[i].start();
		}
	}

}
