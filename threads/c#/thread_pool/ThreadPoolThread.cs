using System;
using System.Threading;

public class ThreadPoolThread
{
    private Thread _thread;
    private Action? _action;
    private bool _isRunning;

    public ThreadPoolThread()
    {
        _isRunning = true;
        _thread = new Thread(Run);
        _thread.Start();
    }

    public void Enqueue(Action action)
    {
        _action = action;
    }

    private void Run()
    {
        while (_isRunning)
        {
            if (_action != null)
            {
                _action();
                _action = null;
            }
            else
            {
                Thread.Sleep(100);
            }
        }
    }

    public void Stop()
    {
        _isRunning = false;
    }
}
