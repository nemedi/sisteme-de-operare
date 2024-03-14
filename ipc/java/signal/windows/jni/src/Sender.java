import java.nio.file.Paths;

public class Sender {
    static {
        System.load(Paths.get("./bin/sender.dll").toFile().getAbsolutePath());
    }
    public native static long openEvent(String eventName);
    public native static int setEvent(long eventHandle);
    public native static int closeHandle(long eventHandle);

    public static void main(String[] args) {
        long eventHandle = openEvent("MyEvent");
        if (eventHandle != 0) {
            System.out.println("Event opened successfully with handle: " + eventHandle);
            int setResult = setEvent(eventHandle);
            if (setResult == 0) {
                System.out.println("Event set successfully.");
            } else {
                System.out.println("Failed to set event.");
            }
            int closeResult = closeHandle(eventHandle);
            if (closeResult == 0) {
                System.out.println("Event handle closed successfully.");
            } else {
                System.out.println("Failed to close event handle.");
            }
        } else {
            System.out.println("Failed to open event.");
        }
    }
}
