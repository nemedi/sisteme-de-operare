package cfs.client;

import java.nio.file.Paths;
import java.util.Scanner;

public class ClientApplication {
    
	public static void main(String[] args) throws Exception {
		String mountPoint = "/mnt/" + ClientConfiguration.NAME;
		ClusterFS fs = new ClusterFS(ClientConfiguration.ENDPOINT_MAP);
		fs.mount(Paths.get(mountPoint), false, false, new String[]{
				"-o", "rw",
				"-o", "fsname=" + ClientConfiguration.NAME,
				"-o", "allow_other",
				"-o", "default_permissions"
		 });
		System.out.println(String.format("Cluster FileSystem is mounted to '%s',  type 'exit' to unmount it.",
				mountPoint));
		String command = null;
		try (Scanner scanner = new Scanner(System.in)) {
        	while (true) {
        		if (scanner.hasNextLine()) {
        			command = scanner.nextLine();
        		}
        		if (command == null || "exit".equals(command)) {
        			break;
        		}
        	}
        } finally {
        	fs.umount();
        }
    }
}
