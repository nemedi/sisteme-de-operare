package cfs;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClusterContract extends Remote {

	String NAME = "clusterService";
	
	String[] getRootDirectories() throws RemoteException;
}
