package server;

import java.util.ResourceBundle;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle("settings");
		int port = Integer.parseInt(bundle.getString("port"));
		try (Server server = new Server(port);
				Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String command = scanner.nextLine();
				if (command == null || "exit".equalsIgnoreCase(command)) {
					break;
				}
			}
		} catch (Exception e) {
		}
		System.exit(0);
	}

}
