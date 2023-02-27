package chat.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import chat.common.Message;

public class Client implements AutoCloseable {
	
	private Socket socket;

	@SuppressWarnings("incomplete-switch")
	public Client(String host, int port, ClientCallback callback)
			throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		new Thread(() -> {
			while (!socket.isClosed()) {
				try {
					Message message = Message.read(socket);
					switch (message.getType()) {
					case ACCEPT:
						callback.onAccept(message.getArguments());
						break;
					case DENYY:
						callback.onDeny();
						break;
					case RECEIVE:
						callback.onReceive(message.getArguments()[0],
								message.getArguments()[1]);
						break;
					case EXIT:
						callback.onExit();
						break;
					case ADD_USER:
						callback.onAddUser(message.getArgument());
						break;
					case REMOVE_USER:
						callback.onRemoveUser(message.getArgument());
						break;
					}
				} catch (IOException e) {
				}
			}
		}).start();
	}
	
	public void login(String name) throws IOException {
		Message.login(name).write(socket);
	}
	
	public void send(String to, String text) throws IOException {
		Message.send(to, text).write(socket);
	}
	
	public void logout() throws IOException {
		Message.logout().write(socket);
	}
	
	@Override
	public void close() throws Exception {
		if (!socket.isClosed()) {
			socket.close();
		}
	}
}
