package chat.common;

import java.io.IOException;
import java.net.Socket;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Message {

	private MessageTypes type;
	private String data;
	
	private Message(MessageTypes type, String data) {
		this.type = type;
		this.data = data;
	}
	
	public static Message read(Socket socket) throws IOException {
		MessageTypes type = MessageTypes.values()[socket.getInputStream().read()];
		byte[] buffer = new byte[socket.getInputStream().read()];
		int count = 0;
		while (count < buffer.length) {
			count += socket.getInputStream().read(buffer, count, buffer.length);
		}
		return new Message(type, buffer.length > 0 ? new String(buffer) : null);
	}
	
	public MessageTypes getType() {
		return type;
	}
	
	public String getArgument() {
		return data;
	}
	
	public String[] getArguments() {
		return data.split(",");
	}
	
	public void write(Socket socket) throws IOException {
		socket.getOutputStream().write(type.ordinal());
		if (data != null) {
			byte[] buffer = data.getBytes();
			socket.getOutputStream().write(buffer.length);
			socket.getOutputStream().write(buffer);
			
		} else {
			socket.getOutputStream().write(0);
		}
		socket.getOutputStream().flush();
	}
	
	public static Message login(String name) {
		return new Message(MessageTypes.LOGIN, name);
	}
	
	public static Message deny() {
		return new Message(MessageTypes.DENYY, null);
	}
	
	public static Message accept(String[] names) {
		return new Message(MessageTypes.ACCEPT,
				Stream.of(names).collect(Collectors.joining(",")));
	}
	
	public static Message addUser(String name) {
		return new Message(MessageTypes.ADD_USER, name);
	}
	
	public static Message removeUser(String name) {
		return new Message(MessageTypes.REMOVE_USER, name);
	}
	
	public static Message send(String to, String text) {
		return new Message(MessageTypes.SEND, Stream.of(to, text).collect(Collectors.joining(",")));
	}
	
	public static Message receive(String from, String text) {
		return new Message(MessageTypes.RECEIVE, Stream.of(from, text).collect(Collectors.joining(",")));
	}
	
	public static Message logout() {
		return new Message(MessageTypes.LOGOUT, null);
	}
	
	public static Message exit() {
		return new Message(MessageTypes.EXIT, null);
	}
	
}
