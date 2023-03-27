package problem;

import java.util.concurrent.Semaphore;

public class Program {

	public static void main(String[] args) {
		final int n = Runtime.getRuntime().availableProcessors();
		FreeChairs freeChairs = new FreeChairs(n);
		Semaphore clients = new Semaphore(0);
		Semaphore barberReady = new Semaphore(0);
		Semaphore mutex = new Semaphore(1);
		Thread barber = new Thread(new Barber(freeChairs, clients, barberReady, mutex));
		barber.start();
		for (int i = 0; i < n; i++) {
			Thread client = new Thread(new Client(i, freeChairs, clients, barberReady, mutex));
			client.start();
		}
	}

}
