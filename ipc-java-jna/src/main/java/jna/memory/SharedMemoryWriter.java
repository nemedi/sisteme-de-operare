package jna.memory;

import jna.MyPosixLibrary;
import jna.SharedMemory;
import java.util.Scanner;
import com.sun.jna.Pointer;

public class SharedMemoryWriter {
    public static void main(String[] args) {
        int key = 1234;
        int size = 1024;
        int shmflg = 0666 | MyPosixLibrary.IPC_CREAT;
        SharedMemory sharedMemory = new SharedMemory(key, size, shmflg);
        try {
        	System.out.println("Type something to be shared or 'quit' to quit.");
            Pointer memoryPointer = sharedMemory.getPointer();
            try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					if (scanner.hasNextLine()) {
						String command = scanner.nextLine();
						if ("quit".equals(command)) {
							break;
						} else {
							memoryPointer.setString(0, command);
						}
					}
				}
            }
        } finally {
            sharedMemory.detach();
        }
    }
}
