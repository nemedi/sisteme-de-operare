import win32event
import win32api
import pywintypes

EVENT_NAME = "Global\\MyEvent"

def event_receiver():
    print("Waiting for event...")

    # Open or create an event
    event_handle = win32event.CreateEvent(None, False, False, EVENT_NAME)

    if event_handle is None:
        print("Failed to create event")
        return

    try:
        while True:
            print("Waiting for signal...")
            result = win32event.WaitForSingleObject(event_handle, win32event.INFINITE)

            if result == win32event.WAIT_OBJECT_0:
                print("Event received! Processing...")
    except KeyboardInterrupt:
        print("Receiver stopped.")
    finally:
        win32api.CloseHandle(event_handle)

if __name__ == "__main__":
    event_receiver()
