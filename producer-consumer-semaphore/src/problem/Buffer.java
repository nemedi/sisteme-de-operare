package problem;

public class Buffer {

	private int[] array;
	private int count;
	
	public Buffer(int size) {
		array = new int[size];
		count = 0;
	}
	
	public int getSize() {
		return array.length;
	}
	
	public void put(int item) {
		array[count++] = item;
		System.out.println("put: " + item);
	}
	
	public int take() {
		int item = array[--count];
		System.out.println("take: " + item);
		return item;
	}
	
	public boolean isEmpty() {
		return count == 0;
	}
	
	public boolean isFull() {
		return count == array.length;
	}
}
