package ipc;

import java.nio.file.Paths;

public class PipeTool {
	
	private static final int MODE = 0666;
	
	static {
        System.load(Paths.get("./lib/pipe.so").toFile().getAbsolutePath());
    }
    
    public static native int createPipe(String path, int mode);
    
    public static int createPipe(String path) {
    	return createPipe(path, MODE);
    }
    
}
