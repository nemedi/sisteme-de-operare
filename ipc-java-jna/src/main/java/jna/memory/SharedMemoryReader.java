package jna.memory;

import java.util.Scanner;
import com.sun.jna.Pointer;
import jna.SharedMemory;

public class SharedMemoryReader {
	
    public static void main(String[] args) {
        int key = 1234;
        int size = 1024;
        SharedMemory sharedMemory = new SharedMemory(key, size, 0666);
        try {
            Pointer pointer = sharedMemory.getPointer();
            new Thread(() -> {
            	try {
					while (true) {
	            		String data = pointer.getString(0);
						if (data != null && !data.isEmpty()) {
							pointer.clear(size);
							System.out.println("Reader received: " + data);
						}
						Thread.sleep(200);
					}
				} catch (InterruptedException e) {
				}
            }).start();
            System.out.println("Type 'quit' to quit.");
            try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					if (scanner.hasNextLine()) {
						String command = scanner.nextLine();
						if ("quit".equals(command)) {
							break;
						} else {
							pointer.setString(0, command);
						}
					}
				}
            }
        } finally {
            sharedMemory.detach();
            sharedMemory.removeSegment();
            System.exit(0);
        }
    }
}
