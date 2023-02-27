package ipc;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Consumer implements Settings {
	public static void main(String args[]) throws IOException {
		try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "r")) {
			FileChannel channel = file.getChannel();
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, 1000);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int value = buffer.get();
			while (value != 0) {
				System.out.println("Process 2: " + value);
				value = buffer.get();
			}
		}
	}
}
