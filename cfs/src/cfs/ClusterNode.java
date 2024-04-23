package cfs;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClusterNode {

	public static void main(String[] args) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Registry registry = getRegistry(Settings.REGISTRY_PORT);
			Arrays.stream(Settings.NODES)
				.forEach(uri -> {
					try {
						FileSystems.newFileSystem(uri, map);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			boolean hidden = new File("cfs://localhost/D:/Work").isHidden();
			System.out.println(hidden);
			new ClusterService(registry);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Registry getRegistry(int port) throws RemoteException {
		try {
			return LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			return LocateRegistry.getRegistry(port);
		}
	}

}
