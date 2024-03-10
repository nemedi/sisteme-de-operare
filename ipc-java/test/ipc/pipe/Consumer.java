package ipc.pipe;

import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Consumer {

	public static void main(String[] args) {
		try {
			Path path = Paths.get(Settings.PIPE_NAME);
			System.out.println("Waiting to receive data from pipe: " + Settings.PIPE_NAME);
			try (RandomAccessFile file = new RandomAccessFile(path.toFile().getAbsoluteFile(), "r")) {
				while (true) {
					int length = file.read();
					if (length > 0) {
						byte[] bytes = new byte[length];
						file.read(bytes);
						System.out.println("Received: " + new String(bytes));
					} else {
						Thread.sleep(100);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}