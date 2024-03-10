using System;
using System.Runtime.InteropServices;
using System.Threading;

class Program
{
    [DllImport("kernel32.dll", SetLastError = true)]
    static extern IntPtr CreateEvent(IntPtr lpEventAttributes, bool bManualReset, bool bInitialState, string lpName);
    [DllImport("kernel32.dll", SetLastError = true)]
    static extern uint WaitForSingleObject(IntPtr hHandle, uint dwMilliseconds);
    [DllImport("kernel32.dll", SetLastError = true)]
    [return: MarshalAs(UnmanagedType.Bool)]
    static extern bool CloseHandle(IntPtr hObject);
    const uint EVENT_ALL_ACCESS = 0x1F0003;
    const uint WAIT_OBJECT_0 = 0;

    static void Main()
    {
        IntPtr hEvent = CreateEvent(IntPtr.Zero, true, false, "MyEvent");
        if (hEvent == IntPtr.Zero)
        {
            Console.WriteLine("Error creating event. Error code: " + Marshal.GetLastWin32Error());
            return;
        }
        Console.WriteLine("Waiting for event...");
        uint result = WaitForSingleObject(hEvent, uint.MaxValue);
        switch (result)
        {
            case WAIT_OBJECT_0:
                Console.WriteLine("Event signaled successfully.");
                break;
            case 0x102:
                Console.WriteLine("Timeout occurred while waiting for the event.");
                break;
            default:
                Console.WriteLine("Error waiting for the event. Error code: " + Marshal.GetLastWin32Error());
                break;
        }
        CloseHandle(hEvent);
    }
}
