import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static java.util.Collections.synchronizedList;

public class Server implements AutoCloseable {

	private ServerSocket serverSocket;
	
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		ExecutorService executorService =
				Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());
		final List<PrintWriter> writers = synchronizedList(new ArrayList<PrintWriter>());
		executorService.execute(() -> {
			while (!serverSocket.isClosed()) {
				try {
					final Socket socket = serverSocket.accept();
					final BufferedReader reader = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					final PrintWriter writer = new PrintWriter(socket.getOutputStream());
					executorService.execute(() -> {
						try {
							writers.add(writer);
							writer.print("Welcome to Talk, enter message or 'exit' to quit.\r\n");
							writer.flush();
							while (!socket.isClosed()) {
								try {
									String message = reader.readLine();
									if ("exit".equals(message)) {
										socket.close();
									} else {
										writers.forEach(w -> {
											w.print(message + "\r\n");
											w.flush();
										});									
									}
								} catch (Exception e) {
								}
							}
						} catch (Exception e) {
						} finally {
							writers.remove(writer);
						}
					});
				} catch (Exception e) {
				}
			}
		});
	}

	@Override
	public void close() throws Exception {
		serverSocket.close();
	}
	
	public static void main(String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle("settings");
		int port = Integer.parseInt(args.length == 1
				? args[0]
				: bundle.getString("port"));
		System.out.println(String.format("Server is running on port %d, type 'exit' to close it.", port));
		try (Server server = new Server(port);
				Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String command = scanner.nextLine();
				if (command == null || "exit".equalsIgnoreCase(command)) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}