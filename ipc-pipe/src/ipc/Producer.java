package ipc;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Producer {
	public static void main(String[] args) {
		try {
			Runtime.getRuntime().exec("mkfifo " + Settings.FILE_NAME);
			try (RandomAccessFile file = new RandomAccessFile(Settings.FILE_NAME, "rw");
					Scanner scanner = new Scanner(System.in)) {
				System.out.println("Type message or 'exit' to quit.");
				while (true) {
					String command = scanner.nextLine();
					if (command == null || "exit".equalsIgnoreCase(command)) {
						break;
					}
					byte[] bytes = command.getBytes();
					file.write(bytes.length);
					file.write(bytes);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}
