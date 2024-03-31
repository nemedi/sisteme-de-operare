import java.util.concurrent.Semaphore;

public class Program {

	public static void main(String[] args) {
		final int n = Runtime.getRuntime().availableProcessors();
		Semaphore[] forks = new Semaphore[n];
		for (int i = 0; i < forks.length; i++) {
			forks[i] = new Semaphore(1);
		}
		for (int i = 0; i < n; i++) {
			var philosopher = new Philosopher(i + 1, forks[i],
					forks[(i + 1) % forks.length]);
			new Thread(philosopher).start();
		}
	}

}
