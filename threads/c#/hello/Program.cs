using System.Threading;
internal class Program
{
    private static void Main(string[] args)
    {
        int n = 10;
        for (int i = 0; i < n; i++) {
            Thread thread  = new Thread(new ThreadStart(Worker));
            thread.Name = "Thread" + (i + 1).ToString();
            thread.Start();
        }
    }

    private static void Worker()
    {
        Console.WriteLine(Thread.CurrentThread.Name);
    }
}

