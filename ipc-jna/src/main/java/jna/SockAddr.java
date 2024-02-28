package jna;

import com.sun.jna.Structure;

public class SockAddr extends Structure {
	
	public static final int AF_UNIX = 1;
	
    public short sun_family;
    public byte[] sun_path = new byte[108];

    public SockAddr() {
        sun_family = AF_UNIX;
    }

    public SockAddr(String path) {
        sun_family = AF_UNIX;
        System.arraycopy(path.getBytes(),
        		0,
        		sun_path,
        		0,
        		Math.min(path.length(), sun_path.length));
    }
}