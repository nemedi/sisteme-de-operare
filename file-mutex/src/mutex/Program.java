package mutex;

import java.io.IOException;
import java.util.Scanner;

public class Program {

	public static void main(String[] arguments) {
		FileMutex mutex = new FileMutex("lock");
		System.out.println("Type 'run' to get echo or 'exit' to quit.");
		try (final Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String command = scanner.nextLine();
				if ("exit".equalsIgnoreCase(command)) {
					break;
				} else if ("run".equalsIgnoreCase(command)) {
					try {
						String result = mutex.doWithLock(prompt -> {
							System.out.print(prompt);
							return scanner.nextLine();
						}, "Enter something to get echo back: ");
						System.out.println("Echo: " + result);
					} catch (IOException e) {
					}
				}
			}
		}
	}

}
