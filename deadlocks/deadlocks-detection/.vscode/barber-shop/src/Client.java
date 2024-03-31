import java.util.concurrent.Semaphore;

public class Client implements Runnable {
	
	private int id;
	private FreeChairs freeChairs;
	private Semaphore clients;
	private Semaphore barberReady;
	private Semaphore chair;
	
	public Client(int id, FreeChairs freeChairs, Semaphore clients, Semaphore barberReady, Semaphore mutex) {
		this.id = id;
		this.freeChairs = freeChairs;
		this.clients = clients;
		this.barberReady = barberReady;
		this.chair = mutex;
	}
	
	public int getId() {
		return id;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
				chair.acquire();
				System.out.println("Client " + id + " is searching for a free chiar.");
				if (freeChairs.getCount() > 0) {
				    freeChairs.decrement();
				    System.out.println("Client " + id + " found a free chair.");
				    clients.release();
				    chair.release();
				    barberReady.acquire();
				    System.out.println("Client " + id + " awaits to get a haircut.");
				} else {
					System.out.println("There are no free chairs.");
				    chair.release();
				    System.out.println("Client " + id + " leaves without getting a haircut.");
				}
			} catch (InterruptedException e) {
				break;
			}
			
		}
	}

}
