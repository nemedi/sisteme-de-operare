package problem;

import java.util.concurrent.Semaphore;

public class Barber implements Runnable {
	
	private FreeChairs freeChairs;
	private Semaphore clients;
	private Semaphore barberReady;
	private Semaphore chiar;

	public Barber(FreeChairs freeChairs, Semaphore clients, Semaphore barberReady, Semaphore mutex) {
		this.freeChairs = freeChairs;
		this.clients = clients;
		this.barberReady = barberReady;
		this.chiar = mutex;
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("Barber is looking for client.");
				clients.acquire();
				System.out.println("Barber inites the client to seat on the chair.");
				chiar.acquire();
				freeChairs.increment();
				barberReady.release();
				System.out.println("Barber is ready to start cutting client's hair.");
				chiar.release();
				cutCustomerHair();
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void cutCustomerHair() throws InterruptedException {
		System.out.println("Barber is cutting the client's hair.");
		Thread.sleep(500);
	}

}
