package notes;

import java.util.Scanner;

public class NoteServer {
	
    public static void main(String[] args) {
    	int port = args.length == 1
    			? Integer.parseInt(args[0]) : 6969;
        try (Server server = new Server(port);
        		NoteService service = new NoteService()) {
            server.publish("notes", service);
            System.out.println("Server is running, type 'exit' to close it.");
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    if (scanner.hasNextLine() && "exit".equals(scanner.nextLine())) {
                        System.exit(0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
