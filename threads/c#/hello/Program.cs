internal class Program
{
    private static void Main(string[] args)
    {
        string threadName = "MyThread";
        Thread thread = new Thread(() =>
            Console.WriteLine("Hello World from {0}!", Thread.CurrentThread.Name));
        thread.Name = threadName;
        thread.Start();
        thread.Join();
    }
}