package problem;

import java.util.concurrent.Semaphore;

public class Philosopher implements Runnable {
	
	private Semaphore leftFork;
	private Semaphore rightFork;
	private int id;
	
	public Philosopher(int id,
			Semaphore leftFork,
			Semaphore rightFork) {
		this.id = id;
		this.leftFork = leftFork;
		this.rightFork = rightFork;
	}

	@Override
	public void run() {
		while (true) {
			try {
				think();
				leftFork.acquire();
				rightFork.acquire();
				eat();
				rightFork.release();
				leftFork.release();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void eat() {
		System.out.println("Philosopher " + id + " is eating.");
	}
	
	private void think() {
		System.out.println("Philosopher " + id + " is thinking.");
	}

}
