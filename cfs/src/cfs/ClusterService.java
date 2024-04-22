package cfs;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ClusterService implements ClusterContract {
	
	public ClusterService(Registry registry) throws RemoteException, MalformedURLException {
		registry.rebind(NAME, UnicastRemoteObject.exportObject(this, Settings.SERVICE_PORT));
	}

	@Override
	public String[] getRootDirectories() throws RemoteException {
		final List<String> directories = new ArrayList<String>();
		ClusterUtilities.getRootDirectories()
			.forEach(directory -> directories.add(directory.toFile().getAbsolutePath()));
		return directories.toArray(new String[directories.size()]);
	}
	

}
