public class Buffer {

	private int[] array;
	private int count;
	
	public Buffer(int size) {
		array = new int[size];
		count = 0;
	}
	
	public void put(int item) {
		System.out.println("count: " + count);
		array[count++] = item;
	}
	
	public int take() {
		System.out.println("count: " + count);
		return array[--count];
	}
	
}
