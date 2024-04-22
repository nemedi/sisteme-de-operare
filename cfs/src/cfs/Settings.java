package cfs;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static java.util.stream.Collectors.toList;

public final class Settings {

	public static final int SERVICE_PORT;
	public static final int REGISTRY_PORT;
	public static final URI[] NODES;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("settings");
		SERVICE_PORT = bundle.containsKey("service.port")
				? Integer.parseInt(bundle.getString("service.port"))
				: 6969;
		REGISTRY_PORT = bundle.containsKey("registry.port")
				? Integer.parseInt(bundle.getString("registry.port"))
				: 1099;
		List<URI> nodes = Arrays.stream(bundle.getString("nodes").split(","))
			.map(node -> URI.create("cfs://" + node))
			.collect(toList());
		NODES = nodes.toArray(new URI[nodes.size()]);
	}
}
