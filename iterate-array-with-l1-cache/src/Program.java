public class Program {
	public static void main(String[] args) {
		int size = 1024 * 1024;
		int[] array = array(size);
		int[] steps = new int[] {1, 16, 32, 64, 128};
		for (int step : steps) {
			long sum = sum(array, step);
			System.out.println("Sum (step " + step + "): " + sum);
		}
	}
	private static int[] array(int size) {
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			array[i] = i;
		}
		return array;
	}
	public static long sum(int[] array, int step) {
		long startTime = System.nanoTime();
		long sum = 0;
		for (int i = 0; i < array.length; i+= step) {
			sum += array[i];
		}
		long endTime = System.nanoTime();
		System.out.println("Timespan (step " + step + "): " + (endTime - startTime) / 1_000_000.0 + " ms");
		return sum;
	}
}
