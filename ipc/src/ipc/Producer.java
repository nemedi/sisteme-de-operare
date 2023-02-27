package ipc;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Producer implements Settings {
	
	public static void main(String args[]) throws IOException, InterruptedException {
		try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
			FileChannel channel = file.getChannel();
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1000);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 1; i < 10; i++) {
				buffer.put((byte) i);
				System.out.println("Process 1: " + (byte) i);
				Thread.sleep(100);
			}
		}
	}
}
