package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
						writers.add(writer);
						writer.print("Type something to get echo back or 'exit' to quit.\r\n");
						writer.flush();
						while (!socket.isClosed()) {
							reader.lines().forEach(l ->
								writers.forEach(w -> {
									if (w != writer) {
										w.print(l + "\r\n");
										w.flush();
									}
								}));
						}
						writers.remove(writer);
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

}
