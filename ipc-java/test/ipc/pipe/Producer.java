package ipc.pipe;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Scanner;

import ipc.PipeTool;

public class Producer {
	public static void main(String[] args) {
		try {
			Path path = Paths.get(Settings.PIPE_NAME); 
			if (path.toFile().isFile()) {
				Files.delete(path);
			}
			int pipe = PipeTool.createPipe(Settings.PIPE_NAME);
			if(pipe == 0) {
				try (RandomAccessFile file = new RandomAccessFile(path.toFile().getAbsolutePath(), "rw");
						Scanner scanner = new Scanner(System.in)) {
					System.out.println("Sending data to pipe: " + Settings.PIPE_NAME);
					System.out.println("Type a message or 'quit' to quit.");
					while (true) {
						if (scanner.hasNextLine()) {
							String command = scanner.nextLine();
							if (command == null || "quit".equalsIgnoreCase(command)) {
								break;
							} else {
								byte[] bytes = command.getBytes();
								file.write(bytes.length);
								file.write(bytes);
							}
						} else {
							Thread.sleep(100);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			} else {
				System.err.println(MessageFormat.format("Could not create pipe (pipe = {0}).", pipe));
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}