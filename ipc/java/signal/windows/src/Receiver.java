    import com.sun.jna.platform.win32.WinNT.HANDLE;
    import com.sun.jna.Native;
    public class Receiver {
        public static void main(String[] arguments) {
            HANDLE hEvent = Kernel32Library.INSTANCE.CreateEventW(null, true, false, "MyEvent");
            if (hEvent == null) {
                System.out.println("Error creating event. Error code: " + Native.getLastError());
                return;
            }
            System.out.println("Waiting for event...");
            int result = Kernel32Library.INSTANCE.WaitForSingleObject(hEvent, Kernel32Library.INFINITE);
            switch (result) {
                case Kernel32Library.WAIT_OBJECT_0:
                    System.out.println("Event signaled successfully.");
                    break;
                case 0x102:
                    System.out.println("Timeout occurred while waiting for the event.");
                    break;
                default:
                    System.out.println("Error waiting for the event. Error code: " + Native.getLastError());
                    break;
            }
            Kernel32Library.INSTANCE.CloseHandle(hEvent);
        }
    }
