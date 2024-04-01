public class Program {
   
    public static void main(String[] args) {
		final int n = 5;
        ThreadPool threadPool = new ThreadPool(n);
        for (int i = 0; i < 2 * n - 1; i++) {
			final int id = i + 1;
            threadPool.submitTask(() -> {
                System.out.println("Task " + id + " executed by: "
					+ Thread.currentThread().getName());
            });
        }
        threadPool.shutdown();
    }
}
