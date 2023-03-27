package problem;

import java.util.LinkedList;
import java.util.Queue;

public class Storage<T> {

	private Queue<T> queue;
	private int capacity;
	private boolean debug;
	private volatile boolean running;
	private final Object FULL_LOCK = new Object();
	private final Object EMPTY_LOCK = new Object();
	
	public Storage(int capacity, boolean debug) {
		this.capacity = capacity;
		this.debug = debug;
		this.queue = new LinkedList<T>();
		this.running = true;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public boolean isEmpty() {
		return queue.size() == 0;
	}
	
	public boolean isFull() {
		return queue.size() == capacity;
	}
	
	public void waitWhenIsFull() throws InterruptedException {
		if (isRunning() && isFull()) {
			synchronized (FULL_LOCK) {
				if (debug) {
					System.out.println(Thread.currentThread().getName() + " is waiting on full queue.");
				}
				FULL_LOCK.wait();
			}
		}
	}
	
	public void waitWhenIsEmpty() throws InterruptedException {
		if (isRunning() && isEmpty()) {
			synchronized (EMPTY_LOCK) {
				if (debug) {
					System.out.println(Thread.currentThread().getName() + " is waiting on empty queue.");
				}
				EMPTY_LOCK.wait();
			}
		}
	}
	
	public void notifyAllIsNoLongerFull() {
		synchronized (FULL_LOCK) {
			if (debug) {
				System.out.println(Thread.currentThread().getName() + " notifies queue is not full.");
			}
			FULL_LOCK.notifyAll();
		}
	}
	
	public void notifyAllIsNoLongerEmpty() {
		synchronized (EMPTY_LOCK) {
			if (debug) {
				System.out.println(Thread.currentThread().getName() + " notifies queue is not empty.");
			}
			EMPTY_LOCK.notifyAll();
		}
	}
	
	public void put(T data) {
		synchronized (queue) {
			queue.add(data);
		}
	}
	
	public T take() {
		synchronized (queue) {
			return queue.poll();
		}
	}

	public void stop( ) {
		running = false;
		if (isEmpty()) {
			notifyAllIsNoLongerFull();
		}
		if (isFull()) {
			notifyAllIsNoLongerEmpty();
		}
	}
}
