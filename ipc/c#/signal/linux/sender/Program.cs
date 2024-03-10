using System.Runtime.InteropServices;

class Program
{
    const int SIGINT = 2;
    [DllImport("libc.so.6", SetLastError = true)]
    static extern int kill(int pid, int sig);
    static void Main(string[] arguments)
    {
        if (arguments.Length == 1)
        {
            int targetProcessId = int.Parse(arguments[0]);
            int result = kill(targetProcessId, SIGINT);
            if (result == 0)
            {
                Console.WriteLine($"Signal sent successfully to process {targetProcessId}.");
            }
            else
            {
                int errno = Marshal.GetLastWin32Error();
                Console.WriteLine($"Error sending signal to process {targetProcessId}. Error code: {errno}");
            }
        }
    }
}
