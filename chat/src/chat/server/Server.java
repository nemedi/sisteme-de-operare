package chat.server;

import static chat.common.Message.accept;
import static chat.common.Message.addUser;
import static chat.common.Message.removeUser;
import static chat.common.Message.deny;
import static chat.common.Message.exit;
import static chat.common.Message.receive;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import chat.common.Message;

public class Server implements AutoCloseable {
	
	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private Map<String, Socket> connections = Collections.synchronizedMap(new HashMap<String, Socket>());

	@SuppressWarnings("incomplete-switch")
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(20 * Runtime.getRuntime().availableProcessors());
		executorService.execute(() -> {
			while (!serverSocket.isClosed()) {
				try {
					final Socket socket = serverSocket.accept();
					executorService.execute(() -> {
						while (!socket.isClosed()) {
							try {
								Message message = Message.read(socket);
								switch (message.getType()) {
								case LOGIN:
									handleLogin(socket, message.getArgument());
									break;
								case LOGOUT:
									handleLogout(socket);
									break;
								case SEND:
									handleSend(socket,
											message.getArguments()[0],
											message.getArguments()[1]);
									break;
								}
							} catch (IOException e) {
							}
						}
					});
				} catch (IOException e) {
				}
			}
		});
	}
	
	private void handleLogin(Socket socket, String name) throws IOException {
		if (connections.containsKey(name)) {
			deny().write(socket);
		} else {
			final List<String> names = new ArrayList<String>();
			names.add("*");
			final Message addUser = addUser(name);
			connections.entrySet().forEach(entry -> {
				try {
					names.add(entry.getKey());
					addUser.write(entry.getValue());
				} catch (IOException e) {
				}
			});
			connections.put(name, socket);
			accept(names.toArray(new String[names.size()])).write(socket);
		}
	}
	
	private void handleLogout(Socket socket) throws IOException {
		if (connections.containsValue(socket)) {
			String name = getName(socket).get();
			final Message removeUser = removeUser(name);
			connections.remove(name);
			connections.entrySet().forEach(entry -> {
				try {
					removeUser.write(entry.getValue());
				} catch (IOException e) {
				}
			});
			exit().write(socket);
		}
	}
	
	private void handleSend(Socket socket, String to, String text) throws IOException {
		Optional<String> name = getName(socket);
		if (name.isPresent()) {
			final Message receive = receive(name.get(), text);
			if ("*".equals(to)) {
				connections.entrySet().forEach(entry -> {
					try {
						receive.write(entry.getValue());
					} catch (IOException e) {
					}
				});
			} else if (connections.containsKey(to)) {
				receive.write(connections.get(to));
				receive.write(socket);
			}
		}
	}
	
	private Optional<String> getName(Socket socket) {
		return connections.entrySet()
				.stream()
				.filter(entry -> socket.equals(entry.getValue()))
				.map(entry -> entry.getKey())
				.findAny();
	}
	
	@Override
	public void close() throws Exception {
		serverSocket.close();
		executorService.shutdown();
	}

}
