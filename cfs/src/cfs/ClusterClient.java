package cfs;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ClusterClient {
	
	private ClusterContract stub;

	public ClusterClient(URI uri) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(uri.getHost(), uri.getPort());
		stub = (ClusterContract) registry.lookup(ClusterContract.NAME);
	}
	
	public String getSeparator() {
		return null;
	}
	
	public boolean isReadOnly() {
		return false;
	}
	
	public boolean isOpen() {
		return false;
	}
	
	public Iterable<Path> getRootDirectories() throws RemoteException {
		return Arrays.stream(stub.getRootDirectories())
			.map(directory -> Paths.get(directory))
			.collect(toList());
	}
	
	public Path getPath(String first, String... more) {
		return null;
	}
	
	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		
	}
	
	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		return null;
	}
	
	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		return null;
	}
	
	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) {
		return null;
	}
	
	public Set<String> supportedFileAttributeViews() {
		return null;
	}
	
	public Iterable<FileStore> getFileStores() {
		return null;
	}
	
	public boolean isHidden(Path path) throws IOException {
		return false;
	}
}
