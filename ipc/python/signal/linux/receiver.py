import signal
import os
import time

def handle_signal(signum, frame):
    print(f"Received signal: {signum}")

if __name__ == "__main__":
    print(f"Receiver PID: {os.getpid()}")
    signal.signal(signal.SIGUSR1, handle_signal)
    signal.signal(signal.SIGUSR2, handle_signal)
    print("Waiting for signals...")
    while True:
        time.sleep(1)