package cfs;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Set;

public class ClusterFileSystem extends FileSystem {
	
	private ClusterClient client;
	
	public ClusterFileSystem(URI uri) throws RemoteException, NotBoundException {
		if (uri != null) {
			client = new ClusterClient(uri);
		}
	}
	
	public ClusterFileSystem() throws RemoteException, NotBoundException {
		this(null);
	}
	
	public boolean isLocal() {
		return client != null;
	}

	@Override
	public FileSystemProvider provider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOpen() {
		return client != null
				? client.isOpen()
				: false;
	}

	@Override
	public boolean isReadOnly() {
		return client != null
				? client.isReadOnly()
				: false;
	}

	@Override
	public String getSeparator() {
		return client != null
				? client.getSeparator()
				: System.getProperty("line.separator");
	}

	@Override
	public Iterable<Path> getRootDirectories() {
		try {
			return client != null
				? client.getRootDirectories()
				: ClusterUtilities.getRootDirectories();
		} catch (RemoteException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		return null;
	}

	@Override
	public Path getPath(String first, String... more) {
		return client != null
			? client.getPath(first, more)
			: Paths.get(first, more);
	}

	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WatchService newWatchService() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ClusterClient getClient() {
		return client;
	}
	
	public FileStore getFileStore(Path path) throws IOException {
		return null;
	}
	
	public boolean isHidden(Path path) throws IOException {
		return false;
	}
	
	public void createDirectory(Path path, FileAttribute<?>... attributes) throws IOException {
		
	}
	
	public void delete(Path path) throws IOException {
		
	}
	
	public boolean isSameFile(Path firstPath, Path secondPath) throws IOException {
		return false;
	}

}
