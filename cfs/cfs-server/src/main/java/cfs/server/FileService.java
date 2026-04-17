package cfs.server;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {
	
	@Autowired
	private ServerConfiguration configuration;

    private Path resolve(String path) {
        return Paths.get(configuration.getRoot())
        		.resolve(path.startsWith("/") ? path.substring(1) : path);
    }

    public FileStatDTO stat(String path) throws IOException {
        Path p = resolve(path);
        if (!Files.exists(p)) {
            return null;
        }
        BasicFileAttributes attributes = Files.readAttributes(p, BasicFileAttributes.class);
        FileStatDTO dto = new FileStatDTO();
        dto.directory = attributes.isDirectory();
        dto.size = attributes.size();
        dto.atime = attributes.lastAccessTime().toMillis() / 1000;
        dto.mtime = attributes.lastModifiedTime().toMillis() / 1000;
        dto.ctime = attributes.creationTime().toMillis() / 1000;
        try {
            PosixFileAttributes posix = Files.readAttributes(p, PosixFileAttributes.class);
            dto.mode = toMode(posix.permissions(), dto.directory);
            dto.uid = posix.owner().getName().hashCode();
            dto.gid = posix.group().getName().hashCode();
        } catch (Exception e) {
            dto.mode = dto.directory ? 040755 : 0100644;
            dto.uid = 1000;
            dto.gid = 1000;
        }
        return dto;
    }

    public List<String> list(String path) throws IOException {
        try (Stream<Path> stream = Files.list(resolve(path))) {
            return stream.map(p -> p.getFileName().toString()).toList();
        }
    }

    public byte[] read(String path, long offset, int size) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(resolve(path).toFile(), "r")) {
            file.seek(offset);
            byte[] buf = new byte[size];
            int read = file.read(buf);
            return read == -1 ? new byte[0] : Arrays.copyOf(buf, read);
        }
    }

    public int write(String path, long offset, byte[] data) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(resolve(path).toFile(), "rw")) {
            file.seek(offset);
            file.write(data);
            return data.length;
        }
    }
    
    public void utimens(String path, long atime, long mtime) throws IOException {
        Path p = resolve(path);
        if (!Files.exists(p)) {
            return;
        }
        FileTime at = FileTime.fromMillis(atime * 1000);
        FileTime mt = FileTime.fromMillis(mtime * 1000);
        Files.setAttribute(p, "basic:lastAccessTime", at);
        Files.setAttribute(p, "basic:lastModifiedTime", mt);
    }

    public void create(String path) throws IOException {
        Files.createFile(resolve(path));
    }

    public void delete(String path) throws IOException {
        Path p = resolve(path);
        if (!Files.exists(p)) {
            return;
        }
        Files.delete(p);
    }

    public void mkdir(String path) throws IOException {
        Files.createDirectories(resolve(path));
    }
    
    public void rmdir(String path) throws IOException {
        Path p = resolve(path);
        if (!Files.exists(p)) {
            return;
        }
        deleteRecursive(p);
    }
    
    public void deleteRecursive(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }
        if (Files.isDirectory(path)) {
            try (var entries = Files.list(path)) {
                for (Path entry : entries.toList()) {
                    deleteRecursive(entry);
                }
            }
        }
        Files.delete(path);
    }

    public void rename(String from, String to) throws IOException {
    	Path p = resolve(from);
        if (!Files.exists(p)) {
            return;
        }
        Files.move(p, resolve(to), StandardCopyOption.REPLACE_EXISTING);
    }

    public void truncate(String path, long size) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(resolve(path).toFile(), "rw")) {
            raf.setLength(size);
        }
    }

    private int toMode(Set<PosixFilePermission> perms, boolean dir) {
        int mode = dir ? 040000 : 0100000;
        if (perms.contains(PosixFilePermission.OWNER_READ)) mode |= 0400;
        if (perms.contains(PosixFilePermission.OWNER_WRITE)) mode |= 0200;
        if (perms.contains(PosixFilePermission.OWNER_EXECUTE)) mode |= 0100;
        if (perms.contains(PosixFilePermission.GROUP_READ)) mode |= 040;
        if (perms.contains(PosixFilePermission.GROUP_WRITE)) mode |= 020;
        if (perms.contains(PosixFilePermission.GROUP_EXECUTE)) mode |= 010;
        if (perms.contains(PosixFilePermission.OTHERS_READ)) mode |= 04;
        if (perms.contains(PosixFilePermission.OTHERS_WRITE)) mode |= 02;
        if (perms.contains(PosixFilePermission.OTHERS_EXECUTE)) mode |= 01;
        return mode;
    }
}