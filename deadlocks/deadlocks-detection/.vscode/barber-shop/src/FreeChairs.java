public class FreeChairs {

	private int count;
	
	public FreeChairs(int capacity) {
		this.count = capacity;
	}
	
	public void increment() {
		count++;
	}
	
	public void decrement() {
		count--;
	}
	
	public int getCount() {
		return count;
	}
}
