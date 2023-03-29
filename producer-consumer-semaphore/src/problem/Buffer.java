package problem;

public class Buffer {

	private int[] array;
	private int count;
	
	public Buffer(int size) {
		array = new int[size];
		count = 0;
	}
	
	public void put(int item) {
		array[count++] = item;
	}
	
	public int take() {
		return array[--count];
	}
	
}
