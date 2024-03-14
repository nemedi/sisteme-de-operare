import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.Native;

public class Sender {

    public static void main(String[] arguments) {
        HANDLE hEvent = Kernel32Library.INSTANCE.OpenEventW(Kernel32Library.EVENT_MODIFY_STATE, false, "MyEvent");
        if (hEvent == null) {
            System.err.println("Failed to open event. Error code: " + Native.getLastError());
        } else {
            System.out.println("Event opened successfully!");
            Kernel32Library.INSTANCE.SetEvent(hEvent);
            Kernel32Library.INSTANCE.ResetEvent(hEvent);
            Kernel32Library.INSTANCE.CloseHandle(hEvent);
        }
    }
}
