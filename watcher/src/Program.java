import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Scanner;

public class Program {

	public static void main(String[] arguments) throws IOException, InterruptedException {
		String path = arguments.length == 1
				? arguments[0]
				: System.getProperty("user.home");
		new Thread(() -> {
			try {
				watch(Paths.get(path));
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		System.out.println("Type 'exit' to close it.");
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String command = scanner.nextLine();
				if (command == null || "exit".equals(command)) {
					System.exit(0);
				}
			}
		}
	}
	
	private static void watch(Path path) throws IOException, InterruptedException {
		WatchService watchService = FileSystems.getDefault().newWatchService();
		path.register(watchService,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);
		System.out.println("Watching: " + path.toFile().getAbsolutePath());
		WatchKey key;
		while ((key = watchService.take()) != null) {
			for (WatchEvent<?> event : key.pollEvents()) {
				System.out.println(String.format("Event kind: %s. File affected: %s.",
						event.kind(),
						event.context()));
			}
			key.reset();
		}		
	}

}
