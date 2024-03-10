package ipc;

import java.nio.file.Paths;

public class SemaphoreTool {
	
	private static final int SHARED = 0666;

    static {
        System.load(Paths.get("./lib/semaphore.so").toFile().getAbsolutePath());
    }
    
    public static native long openSemaphore(String name, int shared, int capacity);
    
    public static long openSemaphore(String name, int capacity) {
    	return openSemaphore(name, SHARED, capacity);
    }
    
    public static native long aquireSemaphore(long value);
    
    public static native long releaseSemaphore(long value);
    
    public static native void closeSemaphore(long semaphore);
    
    public static native void unlinkSemaphore(String name);
    
}
