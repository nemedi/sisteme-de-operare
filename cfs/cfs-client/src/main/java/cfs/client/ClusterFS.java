package cfs.client;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import jnr.ffi.Pointer;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseFileInfo;
import ru.serce.jnrfuse.struct.Timespec;

public class ClusterFS extends FuseStubFS {

    private final Map<String, FileSystemRestClient> clientMap;

    public ClusterFS(Map<String, String> endpointMap) {
    	clientMap = endpointMap.entrySet()
    			.stream()
    			.collect(Collectors.toMap(Map.Entry::getKey, e -> new FileSystemRestClient(e.getValue())));
    }
    
	@Override
    public int readdir(String path, Pointer buf, FuseFillDir filler,
                       long offset, FuseFileInfo fi) {
        try {
			filler.apply(buf, ".", null, 0);
			filler.apply(buf, "..", null, 0);
			if (path.equals("/")) {
				clientMap.keySet().forEach(k -> filler.apply(buf, k, null, 0));
			    return 0;
			} else {
				client(path).list(relativePath(path)).forEach(i -> filler.apply(buf, i, null, 0));
			    return 0;
			}
		} catch (Exception e) {
			return -ErrorCodes.ENOENT();
		}
    }

    @Override
    public int getattr(String path, FileStat stat) {
        try {
            if ("/".equals(path)) {
            	stat.st_uid.set(getUid());
		        stat.st_gid.set(getGid());
                stat.st_mode.set(FileStat.S_IFDIR | 0755);
                return 0;
            } else {
            	if (clientMap.keySet()
            			.stream()
            			.filter(k -> path.equals("/" + k))
            			.findAny()
            			.isPresent()) {
            		stat.st_uid.set(getUid());
 		            stat.st_gid.set(getGid());
                    stat.st_mode.set(FileStat.S_IFDIR | 0755);
                    return 0;
            	} else {
		        	FileStatDTO dto = client(path).stat(relativePath(path));
		            stat.st_mode.set(dto.mode);
		            stat.st_size.set(dto.size);
		            stat.st_uid.set(getUid());
		            stat.st_gid.set(getGid());
		            stat.st_atim.tv_sec.set(dto.atime);
		            stat.st_mtim.tv_sec.set(dto.mtime);
		            stat.st_ctim.tv_sec.set(dto.ctime);
		            return 0;
            	}
            }
        } catch (Exception e) {
            return -ErrorCodes.ENOENT();
        }
    }

    @Override
    public int read(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        try {
            byte[] data = client(path).read(relativePath(path), offset, (int) size);
            buf.put(0, data, 0, data.length);
            return data.length;
        } catch (Exception e) {
            return -ErrorCodes.EIO();
        }
    }

    @Override
    public int write(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        try {
            byte[] data = new byte[(int) size];
            buf.get(0, data, 0, (int) size);
            return client(path).write(relativePath(path), offset, data);
        } catch (Exception e) {
            return -ErrorCodes.EIO();
        }
    }

    @Override
    public int create(String path, long mode, FuseFileInfo fi) {
        try {
        	if ("/".equals(path) || path.indexOf("/") == 0 && path.lastIndexOf("/") == 0) {
        		return -ErrorCodes.EIO();
        	} else {
	            client(path).create(relativePath(path));
	            return 0;
        	}
        } catch (Exception e) {
            return -ErrorCodes.EIO();
        }
    }

    @Override
    public int unlink(String path) {
        try {
        	if ("/".equals(path) || path.indexOf("/") == 0 && path.lastIndexOf("/") == 0) {
        		return -ErrorCodes.EIO();
        	} else {
	            client(path).delete(relativePath(path));
	            return 0;
        	}
        } catch (Exception e) {
            return -ErrorCodes.ENOENT();
        }
    }

    @Override
    public int mkdir(String path, long mode) {
        try {
        	if ("/".equals(path) || path.indexOf("/") == 0 && path.lastIndexOf("/") == 0) {
        		return -ErrorCodes.EIO();
        	} else {
	            client(path).mkdir(relativePath(path));
	            return 0;
        	}
        } catch (Exception e) {
            return -ErrorCodes.EIO();
        }
    }
    
    @Override
    public int rmdir(String path) {
        try {
        	if ("/".equals(path) || path.indexOf("/") == 0 && path.lastIndexOf("/") == 0) {
        		return -ErrorCodes.EIO();
        	} else {
	            client(path).rmdir(relativePath(path));
	            return 0;
        	}
        } catch (Exception e) {
            return -ErrorCodes.EIO();
        }
    }

    @Override
    public int rename(String oldPath, String newPath) {
        try {
        	if ("/".equals(oldPath) || oldPath.indexOf("/") == 0 && oldPath.lastIndexOf("/") == 0) {
        		return -ErrorCodes.EIO();
        	} else {
	            client(oldPath).rename(relativePath(oldPath), relativePath(newPath));
	            return 0;
        	}
        } catch (Exception e) {
            return -ErrorCodes.EIO();
        }
    }

    @Override
    public int truncate(String path, long size) {
        try {
            client(path).truncate(relativePath(path), size);
            return 0;
        } catch (Exception e) {
            return -ErrorCodes.EIO();
        }
    }
    
    @Override
    public int utimens(String path, Timespec[] timespec) {
        try {
            long atime = timespec[0].tv_sec.get();
            long mtime = timespec[1].tv_sec.get();
            client(path).utimens(relativePath(path), atime, mtime);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -ErrorCodes.EIO();
        }
    }
    
    private FileSystemRestClient client(String path) throws FileNotFoundException {
    	Optional<FileSystemRestClient> client = clientMap.entrySet()
    		.stream()
    		.filter(e -> path.startsWith("/" + e.getKey()))
    		.map(e -> e.getValue())
    		.findAny();
    	if (client.isPresent()) {
    		return client.get();
    	} else {
    		throw new FileNotFoundException(path);
    	}
    }
    
    private String relativePath(final String path) {
    	Optional<Entry<String, FileSystemRestClient>> entry = clientMap.entrySet()
        		.stream()
        		.filter(e -> path.startsWith("/" + e.getKey()))
        		.findAny();
    	String relativePath = path;
    	if (entry.isPresent()) {
    		relativePath = relativePath.substring(entry.get().getKey().length() + 1);
    		if (relativePath.isEmpty()) {
    			relativePath = "/";
    		}
    	}
    	return relativePath;
    }
    
    public static int getUid() {
    	try {
            Process p = new ProcessBuilder("id", "-u").start();
            String result = new String(p.getInputStream().readAllBytes()).trim();
            return Integer.parseInt(result);
        } catch (Exception e) {
            throw new RuntimeException("Cannot get UID", e);
        }
    }
    
    public static int getGid() {
        try {
            Process p = new ProcessBuilder("id", "-g").start();
            String result = new String(p.getInputStream().readAllBytes()).trim();
            return Integer.parseInt(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}