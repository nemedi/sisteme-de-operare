package rpc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcServer implements AutoCloseable {

	private class Worker implements Runnable {

		private Socket socket;

		public Worker(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				if (socket != null && !socket.isClosed()) {
					while (socket != null && !socket.isClosed()) {
						try {
							if (socket.getInputStream().available() > 0) {
								RpcRequest request = RpcTransport.receive(socket, RpcRequest.class);
								RpcResponse response = new RpcResponse();
								if (request != null) {
									String service = request.getService();
									if (services.containsKey(service)) {
										services.get(service)
											.process(request, response);
									} else {
										response.setFault("Service unavailable.");
									}
									RpcTransport.send(response, socket);
								} else {
									response.setFault("Bad request.");								
								}
							}
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
			}
		}

	}

	private ServerSocket socket;
	private ExecutorService executorService;
	private Map<String, RpcService> services;
	
	public RpcServer() {
		services = new HashMap<String, RpcService>();
	}
	
	public void publish(String name, Class<?> type) {
		services.put(name, new RpcService(type));
	}
	
	public void publish(String name, Object instance) {
		services.put(name, new RpcService(instance));
	}
	
	public void unpublish(String name) {
		if (services.containsKey(name)) {
			services.remove(name);
		}
	}
	
	public void start(String host, int port) throws IOException {
		stop();
		executorService = Executors.newFixedThreadPool(
				10 * Runtime.getRuntime().availableProcessors());
		socket = new ServerSocket(port, 10, InetAddress.getByName(host));
		executorService.submit(() -> {
			try {
				while (socket != null && !socket.isClosed()) {
					executorService.submit(new Worker(socket.accept()));
				}
			} catch (IOException e) {
			}
		});
	}

	public void stop() throws IOException {
		if (socket != null && !socket.isClosed()) {
			socket.close();
			socket = null;
		}
		if (executorService != null) {
			executorService.shutdown();
			executorService = null;
		}
	}

	@Override
	public void close() throws Exception {
		stop();
	}
}
