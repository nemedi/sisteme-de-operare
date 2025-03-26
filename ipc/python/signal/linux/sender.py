import os
import signal
import sys

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python sender.py <PID> <SIGNAL>")
        print("SIGNAL: 1 for SIGUSR1, 2 for SIGUSR2")
        sys.exit(1)
    pid = int(sys.argv[1])
    signal_type = int(sys.argv[2])
    if signal_type == 1:
        os.kill(pid, signal.SIGUSR1)
    elif signal_type == 2:
        os.kill(pid, signal.SIGUSR2)
    else:
        print("Invalid signal. Use 1 for SIGUSR1 or 2 for SIGUSR2.")
        sys.exit(1)
    print(f"Sent signal {signal_type} to process {pid}")