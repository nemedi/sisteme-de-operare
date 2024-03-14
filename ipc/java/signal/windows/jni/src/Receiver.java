import java.nio.file.Paths;

public class Receiver {
    static {
        System.load(Paths.get("./bin/receiver.dll").toFile().getAbsolutePath());
    }
    public native static long createEvent(boolean manualReset, boolean initialState, String eventName);
    public native static int waitForSingleObject(long eventHandle, int milliseconds);
    public native static int closeHandle(long eventHandle);

    public static void main(String[] args) {
        long eventHandle = createEvent(false, true, "MyEvent");
        if (eventHandle != 0) {
            System.out.println("Event created successfully with handle: " + eventHandle);
            int waitResult = waitForSingleObject(eventHandle, Integer.MAX_VALUE);
            switch (waitResult) {
                case 0: // WAIT_OBJECT_0
                    System.out.println("Event signaled.");
                    break;
                case 0x102: // WAIT_TIMEOUT
                    System.out.println("Wait timed out.");
                    break;
                default:
                    System.out.println("Wait failed with error code: " + waitResult);
            }
            int closeResult = closeHandle(eventHandle);
            if (closeResult == 0) {
                System.out.println("Event handle closed successfully.");
            } else {
                System.out.println("Failed to close event handle.");
            }
        } else {
            System.out.println("Failed to create event.");
        }
    }
}
