public class Buffer {
	private int[] array;
	private int count = 0;
	private final Object putLock;
	private final Object takeLock;
	public Buffer(int size) {
		array = new int[size];
		putLock = new Object();
		takeLock = new Object();
	}
	private boolean isFull() {
		return count == array.length;
	}
	private boolean isEmpty() {
		return count == 0;
	}
	private void internalPut(int item) {
		synchronized (array) {
			array[count++] = item;
		}
	}
	private int internalTake() {
		synchronized (array) {
			return array[--count];
		}
	}
	private void wait(Object lock) throws InterruptedException {
		synchronized (lock) {
			lock.wait();
		}
	}
	private void notifyAll(Object lock) throws InterruptedException {
		synchronized (lock) {
			lock.notifyAll();
		}
	}
	public void put(int item) throws InterruptedException {
		if (isFull()) {
			wait(putLock);
		}
		internalPut(item);
		notifyAll(takeLock);
	}
	public int take() throws InterruptedException {
		if (isEmpty()) {
			wait(takeLock);
		}
		int item = internalTake();
		notifyAll(putLock);
		return item;
	}
}
