using System;
using System.Threading;

class Program
{
    static int[] array = new int[100];
    static int numThreads = 4;
    static int chunkSize;
    static AutoResetEvent[] doneEvents = [];

    static void Main()
    {
        Random rand = new Random();
        for (int i = 0; i < array.Length; i++)
        {
            array[i] = rand.Next(1000);
        }
        chunkSize = array.Length / numThreads;
        doneEvents = new AutoResetEvent[numThreads];
        for (int i = 0; i < numThreads; i++)
        {
            doneEvents[i] = new AutoResetEvent(false);
            ThreadPool.QueueUserWorkItem(SortChunk, i);
        }
        WaitHandle.WaitAll(doneEvents);
        MergeChunks();
        Console.WriteLine("Sorted Array:");
        foreach (int item in array)
        {
            Console.Write(item + " ");
        }
        Console.WriteLine();
    }

    static void SortChunk(object threadIndex)
    {
        int index = (int)threadIndex;
        int start = index * chunkSize;
        int end = (index == numThreads - 1) ? array.Length : (index + 1) * chunkSize;
        Array.Sort(array, start, end - start);
        doneEvents[index].Set();
    }

    static void MergeChunks()
    {
        int[] temp = new int[array.Length];
        int chunkIndex = 0;
        for (int i = 0; i < array.Length; i += chunkSize)
        {
            Array.Copy(array, i, temp, chunkIndex, chunkSize);
            chunkIndex += chunkSize;
        }
        Array.Sort(temp);
        Array.Copy(temp, array, array.Length);
    }
}
