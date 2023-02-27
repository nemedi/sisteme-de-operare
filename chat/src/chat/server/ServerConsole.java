package chat.server;

import java.util.Scanner;

import chat.common.Settings;

public class ServerConsole {

	public static void main(String[] args) {
		try (Server server = new Server(Settings.PORT);
				Scanner scanner = new Scanner(System.in)) {
			System.out.println("Server is running, type 'exit' to stop it.");
			while (true) {
				if (scanner.hasNextLine()
						&& "exit".equals(scanner.nextLine())) {
					System.exit(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
