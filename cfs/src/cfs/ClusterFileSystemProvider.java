package cfs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClusterFileSystemProvider extends FileSystemProvider {
	
	private final Map<String, ClusterFileSystem> fileSystemCache =
			Collections.synchronizedMap(new HashMap<String, ClusterFileSystem>());

	@Override
	public String getScheme() {
		return "cfs";
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		try {
			String host = uri.getHost();
			ClusterFileSystem fileSystem = new ClusterFileSystem(isLocal(host) ? null : uri);
			fileSystemCache.put(host, fileSystem);
			return fileSystem;
		} catch (RemoteException | NotBoundException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		return fileSystemCache.get(uri.getHost());
	}

	@Override
	public Path getPath(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createDirectory(Path path, FileAttribute<?>... attributes) throws IOException {
		String host = path.toUri().getHost();
		if (isLocal(host)) {
			Files.createDirectory(path, attributes);
		} else if (fileSystemCache.containsKey(host)) {
			fileSystemCache.get(host).createDirectory(path, attributes);
		}
	}

	@Override
	public void delete(Path path) throws IOException {
		String host = path.toUri().getHost();
		if (isLocal(host)) {
			Files.delete(path);
		} else if (fileSystemCache.containsKey(host)) {
			fileSystemCache.get(host).delete(path);
		}
	}

	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void move(Path source, Path target, CopyOption... options) throws IOException {
		String sourceHost = source.toUri().getHost();
		String targetHost = target.toUri().getHost();
		if (isLocal(sourceHost) && isLocal(targetHost)) {
			Files.move(source, target, options);
		} else {
			
		}
	}

	@Override
	public boolean isSameFile(Path firstPath, Path secondPath) throws IOException {
		String firstHost = firstPath.toUri().getHost();
		String secondHost = secondPath.toUri().getHost();
		if (isLocal(firstHost) && isLocal(secondHost)) {
			return Files.isSameFile(firstPath, secondPath);
		} else if (firstHost.equalsIgnoreCase(secondHost)
				&& fileSystemCache.containsKey(firstHost)) {
			return fileSystemCache.get(firstHost).isSameFile(firstPath, secondPath);
		} else {
			return false;
		}
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		String host = path.toUri().getHost();
		if (isLocal(host)) {
			return Files.isHidden(path);
		} else if (fileSystemCache.containsKey(host)) {
			return fileSystemCache.get(host).isHidden(path);
		} else {
			return false;
		}
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		String host = path.toUri().getHost();
		if (isLocal(host)) {
			return Files.getFileStore(path);
		} else if (fileSystemCache.containsKey(host)) {
			return fileSystemCache.get(host).getFileStore(path);
		} else {
			return null;
		}
	}

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		String host = path.toUri().getHost();
		if (isLocal(host)) {
			return Files.getFileAttributeView(path, type, options);
		} else if (fileSystemCache.containsKey(host)) {
			return fileSystemCache.get(host).getClient().getFileAttributeView(path, type, options);
		} else {
			return null;
		}
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options)
			throws IOException {
		String host = path.toUri().getHost();
		if (isLocal(host)) {
			return Files.readAttributes(path, type, options);
		} else if (fileSystemCache.containsKey(host)) {
			return fileSystemCache.get(host).getClient().readAttributes(path, type, options);
		} else {
			return null;
		}
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		String host = path.toUri().getHost();
		if (isLocal(host)) {
			return Files.readAttributes(path, attributes, options);
		} else if (fileSystemCache.containsKey(host)) {
			return fileSystemCache.get(host).getClient().readAttributes(path, attributes, options);
		} else {
			return Collections.emptyMap();
		}
	}

	@Override
	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		String host = path.toUri().getHost();
		if (isLocal(host)) {
			Files.setAttribute(path, attribute, value, options);
		} else if (fileSystemCache.containsKey(host)) {
			fileSystemCache.get(host).getClient().setAttribute(path, attribute, value, options);
		}
	}

	private boolean isLocal(String host) {
		try {
			return "localhost".equals(host)
					|| ".".equals(host)
					|| "127.0.0.1".equals(host)
					|| InetAddress.getLocalHost().getHostName().equals(host);
		} catch (UnknownHostException e) {
			return false;
		}
	}
	
}
