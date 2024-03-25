using System;
using System.Threading;

class Program
{
    static int[] buffer = new int[10];
    static int count = 0;
    static object monitor = new object();

    static void Producer()
    {
        int item = 0;
        while (true)
        {
            lock (monitor)
            {
                while (count == buffer.Length)
                {
                    Monitor.Wait(monitor);
                }
                buffer[count++] = item++;
                Console.WriteLine("Produced: " + (item - 1));
                Monitor.Pulse(monitor);
            }
        }
    }

    static void Consumer()
    {
        while (true)
        {
            lock (monitor)
            {
                while (count == 0)
                {
                    Monitor.Wait(monitor);
                }
                int item = buffer[--count];
                Console.WriteLine("Consumed: " + item);
                Monitor.Pulse(monitor);
            }
        }
    }

    static void Main(string[] args)
    {
        Thread prodThread = new Thread(Producer);
        Thread consThread = new Thread(Consumer);

        prodThread.Start();
        consThread.Start();

        // Keep the main thread running
        Console.ReadLine();
    }
}
