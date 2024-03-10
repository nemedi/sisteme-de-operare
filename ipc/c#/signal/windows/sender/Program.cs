using System;
using System.Runtime.InteropServices;

class Program
{
    [DllImport("kernel32.dll", SetLastError = true)]
    static extern IntPtr OpenEvent(uint dwDesiredAccess, bool bInheritHandle, string lpName);
    [DllImport("kernel32.dll", SetLastError = true)]
    static extern bool SetEvent(IntPtr hEvent);
    [DllImport("kernel32.dll", SetLastError = true)]
    [return: MarshalAs(UnmanagedType.Bool)]
    static extern bool CloseHandle(IntPtr hObject);
    const uint EVENT_MODIFY_STATE = 0x0002;

    static void Main()
    {
        uint desiredAccess = EVENT_MODIFY_STATE;
        IntPtr hEvent = OpenEvent(desiredAccess, false, "MyEvent");
        if (hEvent == IntPtr.Zero)
        {
            Console.WriteLine("Error opening event. Error code: " + Marshal.GetLastWin32Error());
            return;
        }
        Console.WriteLine("Event opened successfully.");
        if (SetEvent(hEvent))
        {
            Console.WriteLine("Event set successfully.");
        }
        else
        {
            Console.WriteLine("Error setting event. Error code: " + Marshal.GetLastWin32Error());
        }
        CloseHandle(hEvent);
    }
}
