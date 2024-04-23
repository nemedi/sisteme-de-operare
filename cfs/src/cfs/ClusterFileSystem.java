package cfs;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
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
	
	private FileSystemProvider provider;
	private ClusterClient client;
	
	public ClusterFileSystem(URI uri, FileSystemProvider provider) throws RemoteException, NotBoundException {
		this.provider = provider;
		if (uri != null) {
			client = new ClusterClient(uri);
		}
	}
	
	public ClusterFileSystem(FileSystemProvider provider) throws RemoteException, NotBoundException {
		this(null, provider);
	}
	
	public boolean isLocal() {
		return client != null;
	}

	@Override
	public FileSystemProvider provider() {
		return provider;
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
		return client != null
				? client.getFileStores()
				: FileSystems.getDefault().getFileStores();
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		return client != null
			? client.supportedFileAttributeViews()
			: FileSystems.getDefault().supportedFileAttributeViews();
	}

	@Override
	public Path getPath(String first, String... more) {
		// first = URI.create(first).getPath();
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
		return client != null
				? client.isHidden(path)
				: Files.isHidden(path);
	}
	
	public void createDirectory(Path path, FileAttribute<?>... attributes) throws IOException {
		
	}
	
	public void delete(Path path) throws IOException {
		
	}
	
	public boolean isSameFile(Path firstPath, Path secondPath) throws IOException {
		return false;
	}
	
	public Path getPath(URI uri) {
		return null;
	}

}
