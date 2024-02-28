package jna.semaphore;

import java.util.Scanner;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import jna.MyPosixLibrary;

public class Worker {
	
    public static void main(String[] args) {
        Pointer semaphore = new Memory(Native.getNativeSize(int.class));
        System.out.print("Opening semaphore...");
        int result = MyPosixLibrary.INSTANCE.sem_init(semaphore, 0666, 1);
		if (result == 0) {
			System.out.println("done.");
			System.out.println("Type 'a[quire]', 'r[elease]' or 'q[uit']");
			try (Scanner scanner = new Scanner(System.in)) {
				loop: while (true) {
					if (scanner.hasNextLine()) {
						String command = scanner.nextLine();
						switch (command) {
						case "q":
						case "quit":
							break loop;
						case "a":
						case "aquire":
							System.out.print("Aquiring semaphore...");
							result = MyPosixLibrary.INSTANCE.sem_wait(semaphore);
							if (result == 0) {
								System.out.println("done.");
							} else {
								System.out.println("failed.");
							}
							break;
						case "r":
						case "release":
							System.out.print("Releasing semaphore...");
							result = MyPosixLibrary.INSTANCE.sem_post(semaphore);
							if (result == 0) {
								System.out.println("done.");
							} else {
								System.out.println("failed.");
							}
							break;
						}
					}
				}
			} finally {
				System.out.print("Destorying semaphore...");
				result = MyPosixLibrary.INSTANCE.sem_destroy(semaphore);
				if (result == 0) {
					System.out.println("done.");
				} else {
					System.out.println("failed.");
				}
				System.exit(0);
			}
		} else {
			System.err.println("failed.");
		}
//    	final String name = "worker_semaphore";
//        System.out.print("Opening semaphore...");
//        long semaphore = CLibrary.INSTANCE.sem_open(name, 0, 0666, 1);
//		if (semaphore != -1) {
//			System.out.println("done.");
//			System.out.println("Type 'a[quire]', 'r[elease]' or 'q[uit']");
//			try (Scanner scanner = new Scanner(System.in)) {
//				loop: while (true) {
//					if (scanner.hasNextLine()) {
//						String command = scanner.nextLine();
//						switch (command) {
//						case "q":
//						case "quit":
//							break loop;
//						case "a":
//						case "aquire":
//							System.out.print("Aquiring semaphore...");
//							int result = CLibrary.INSTANCE.sem_wait(semaphore);
//							if (result == 0) {
//								System.out.println("done.");
//							} else {
//								System.out.println("failed.");
//							}
//							break;
//						case "r":
//						case "release":
//							System.out.print("Releasing semaphore...");
//							result = CLibrary.INSTANCE.sem_post(semaphore);
//							if (result == 0) {
//								System.out.println("done.");
//							} else {
//								System.out.println("failed.");
//							}
//							break;
//						}
//					}
//				}
//			} finally {
//				System.out.print("Destorying semaphore...");
//				CLibrary.INSTANCE.sem_close(semaphore);
//				int result = CLibrary.INSTANCE.sem_unlink(name);
//				if (result == 0) {
//					System.out.println("done.");
//				} else {
//					System.out.println("failed.");
//				}
//				System.exit(0);
//			}
//		} else {
//			System.err.println("failed.");
//		}
    }
}