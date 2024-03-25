using System.Threading;

class Program
{
    static void Main(string[] args)
    {
        ThreadPool threadPool = new ThreadPool(5);

        for (int i = 0; i < 10; i++)
        {
            int taskNumber = i;
            threadPool.EnqueueTask(() =>
                Console.WriteLine($"Task {taskNumber} executed by thread {Thread.CurrentThread.ManagedThreadId}"));
        }
        Console.ReadLine();
    }
}
