public class Receiver {

	public static void main(String[] args) {
		CLibrary.INSTANCE.signal(CLibrary.SIGINT, signal -> {
			System.out.println("Received signal " + signal + ". Cleaning up and exiting...");
			System.exit(0);
		});
		System.out.println("Waiting for CTRL+C signal.");
		while (true) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}

}
