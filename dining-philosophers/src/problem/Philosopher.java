package problem;

import java.util.concurrent.Semaphore;

public class Philosopher implements Runnable {
	
	private Semaphore[] locks;
	private int id;
	
	public Philosopher(int id, Semaphore[] locks) {
		this.id = id;
		this.locks = locks;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (id < locks.length - 1) {
					locks[id].acquire();
					locks[id + 1].acquire();
					eat();
					locks[id + 1].release();
					locks[id].release();
					think();
				} else {
					locks[locks.length - 1].acquire();
					locks[0].acquire();
					eat();
					locks[0].acquire();
					locks[locks.length - 1].acquire();
					think();
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void eat() throws InterruptedException {
		synchronized (System.out) {
			System.out.print("Philosopher " + (id + 1) + " is eating...");
			Thread.sleep(500);
			System.out.println("done.");
		}
	}
	
	private void think() throws InterruptedException {
		synchronized (System.out) {
			System.out.print("Philosopher " + (id + 1) + " is thinking...");
			Thread.sleep(500);
			System.out.println("done.");
		}
	}

}
