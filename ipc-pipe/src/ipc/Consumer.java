package ipc;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Consumer {

	public static void main(String[] args) {
		try {
			try (RandomAccessFile file = new RandomAccessFile(Settings.FILE_NAME, "r")) {
				while (true) {
					int length = file.read();
					byte[] bytes = new byte[length];
					file.read(bytes);
					System.out.println("Received: " + new String(bytes));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
