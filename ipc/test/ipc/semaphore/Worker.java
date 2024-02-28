package ipc.semaphore;

import java.util.Scanner;

import ipc.SemaphoreTool;

public class Worker {

	public static void main(String[] args) {
		long semaphore = SemaphoreTool.openSemaphore(Settings.SEMAPHORE_NAME,
				Settings.SEMAPHORE_CAPACITY);
		if (semaphore != 0) {
			System.out.println("Type 'a[quire]', 'r[elease]' or 'q[uit']");
			try (Scanner scanner = new Scanner(System.in)) {
				loop: while (true) {
					String command = scanner.nextLine();
					switch (command) {
					case "q":
					case "quit":
						break loop;
					case "a":
					case "aquire":
						System.out.print("Waiting to aquire semaphore...");
						SemaphoreTool.aquireSemaphore(semaphore);
						System.out.println("done.");
						System.out.print("Doing some work...");
						Thread.sleep(Settings.WORK_DURATION);
						System.out.println("done.");
						break;
					case "r":
					case "release":
						System.out.print("Releasing semaphore...");
						SemaphoreTool.releaseSemaphore(semaphore);
						System.out.println("done.");
						break;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				SemaphoreTool.closeSemaphore(semaphore);
				SemaphoreTool.unlinkSemaphore(Settings.SEMAPHORE_NAME);
			}
		} else {
			System.err.println("Error creating or opening semaphore.");
		}
	}
}
