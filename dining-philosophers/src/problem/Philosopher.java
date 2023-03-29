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
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void eat() throws InterruptedException {
		System.out.println("Philosopher "
				+ (id + 1) + " is eating.");
		Thread.sleep(500);
	}
	
	private void think() throws InterruptedException {
		System.out.println("Philosopher "
				+ (id + 1) + " is thinking.");
		Thread.sleep(500);
	}

}
