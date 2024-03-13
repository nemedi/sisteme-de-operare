import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;

public interface Kernel32Library extends Kernel32 {
    Kernel32Library INSTANCE = Native.load("kernel32", Kernel32Library.class);
    HANDLE CreateEventW(Pointer lpEventAttributes, boolean bManualReset, boolean bInitialState, String lpName);
    HANDLE OpenEventW(int dwDesiredAccess, boolean bInheritHandle, String lpName);
    DWORD WaitForSingleObject(HANDLE hHandle, DWORD dwMilliseconds);
    boolean SetEvent(HANDLE hEvent);
    boolean CloseHandle(HANDLE hObject);
    int INFINITE = 0xFFFFFFFF;
    int EVENT_MODIFY_STATE = 0x0002;
    int WAIT_OBJECT_0 = 0;
}
