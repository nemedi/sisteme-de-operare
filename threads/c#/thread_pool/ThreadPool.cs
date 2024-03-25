using System;
using System.Collections.Generic;

public class ThreadPool
{
    private List<ThreadPoolThread> _threads;

    public ThreadPool(int threadCount)
    {
        _threads = new List<ThreadPoolThread>();
        for (int i = 0; i < threadCount; i++)
        {
            _threads.Add(new ThreadPoolThread());
        }
    }

    public void EnqueueTask(Action action)
    {
        foreach (var thread in _threads)
        {
            if (thread != null)
            {
                thread.Enqueue(action);
                return;
            }
        }
    }
}
