using System.Runtime.InteropServices;

class Program
{
    private delegate void SignalHandlerDelegate(int signum);
    [DllImport("libc.so.6", EntryPoint = "signal", SetLastError = true)]
    private static extern IntPtr Signal(int signum, SignalHandlerDelegate handler);
    private const int SIGINT = 2; // Ctrl+C interrupt signal
    static void Main()
    {
        SignalHandlerDelegate handler = new SignalHandlerDelegate(SignalHandler);
        IntPtr result = Signal(SIGINT, handler);
        if (result == IntPtr.Zero)
        {
            Console.WriteLine("Failed to register signal handler (Error code: {0})",
                Marshal.GetLastWin32Error());
            return;
        }
        Console.WriteLine("Waiting for CTRL+C signal.");
        while (true)
        {
            Thread.Sleep(100);
        }
    }
    private static void SignalHandler(int signum)
    {
        Console.WriteLine($"Received signal {signum}. Cleaning up and exiting...");
        Environment.Exit(0);
    }
}
