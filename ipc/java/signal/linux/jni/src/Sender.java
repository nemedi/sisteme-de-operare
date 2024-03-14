import java.nio.file.Paths;

public class Sender {
    static {
        System.load(Paths.get("./bin/sender.so").toFile().getAbsolutePath());
    }

    // Native method declaration
    public native static void kill(int pid, int signal);

    public static void main(String[] arguments) {
        if (arguments.length == 2) {
            int pid = Integer.parseInt(arguments[0]);
            int signal = Integer.parseInt(arguments[1]);
            System.out.print("Sending " + signal + " to " + pid + "...");
            kill(pid, signal);
            System.out.println("done.");
        }
    }
}
