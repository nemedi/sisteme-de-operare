import java.nio.file.Paths;

public class Receiver {

    static {
        System.load(Paths.get("./bin/receiver.so").toFile().getAbsolutePath());
    }
    
    public native void registerSignalHandler(int signal);

    public void handleSignal() {
        System.out.println("received");
        System.out.println("Exiting the application.");
        System.exit(0);
    }

    public static void main(String[] arguments) {
        if (arguments.length == 1) {
            int signal = Integer.parseInt(arguments[0]);
            Receiver receiver = new Receiver();
            System.out.println("Current PID is " + ProcessHandle.current().pid());
            System.out.print("Registering signal handler...");
            receiver.registerSignalHandler(signal);
            System.out.println("done.");
            System.out.print("Waiting for signal " + signal + "...");
            while (true) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
