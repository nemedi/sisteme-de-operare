package rpc;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

class RpcTransport {
	
	private static Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
			.registerTypeAdapter(RpcRequest.class, new RpcRequestDeserializer())
			.registerTypeAdapter(RpcResponse.class, new RpcResponseDeserializer())
			.create();
	
	private static class RpcRequestDeserializer implements JsonDeserializer<RpcRequest> {

		@Override
		public RpcRequest deserialize(JsonElement element, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			String service = element.getAsJsonObject().get("service") != null ?
					element.getAsJsonObject().get("service").getAsString() : null;
			String method = element.getAsJsonObject().get("method") != null ?
					element.getAsJsonObject().get("method").getAsString() : null;
			String session = element.getAsJsonObject().get("session") != null ?
					element.getAsJsonObject().get("session").getAsString() : null;
			List<String> arguments = new ArrayList<String>();
			if (element.getAsJsonObject().get("arguments") != null) {
				for (Iterator<JsonElement> iterator = element.getAsJsonObject().get("arguments").getAsJsonArray().iterator();
						iterator.hasNext(); ) {
					arguments.add(iterator.next().toString());
				}
			}
			return new RpcRequest(service, method, arguments.toArray(), session);
		}
		
	}
	
	private static class RpcResponseDeserializer implements JsonDeserializer<RpcResponse> {

		@Override
		public RpcResponse deserialize(JsonElement element, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			RpcResponse response = new RpcResponse();
			response.setResult(element.getAsJsonObject().get("result") != null ?
					element.getAsJsonObject().get("result").toString() : null);
			response.setFault(element.getAsJsonObject().get("fault") != null ?
					element.getAsJsonObject().get("fault").getAsString() : null);
			response.setSession(element.getAsJsonObject().get("session") != null ?
					element.getAsJsonObject().get("session").getAsString() : null);
			return response;
		}
	}

	public static <T> T deserialize(String data, Class<T> type) {
		return gson.fromJson(data, type);
	}
	
	public static String serialize(Object object) {
		return gson.toJson(object);
	}
	
	public static <T extends Serializable> T receive(Socket socket, Class<T> type)
			throws IOException {
		byte[] buffer = new byte[socket.getInputStream().read()];
		int count = 0;
		while (count < buffer.length) {
			count += socket.getInputStream().read(buffer, count, buffer.length);
		}
		return deserialize(new String(buffer), type);
	}
	
	public static <T extends Serializable> void send(T object, Socket socket)
			throws IOException {
		String data = serialize(object);
		byte[] buffer = data.getBytes();
		socket.getOutputStream().write(buffer.length);
		socket.getOutputStream().write(buffer, 0, buffer.length);
		socket.getOutputStream().flush();
	}
}
