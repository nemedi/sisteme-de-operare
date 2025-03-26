import win32event
import win32api
import pywintypes

EVENT_NAME = "Global\\MyEvent"

def event_sender():
    try:
        event_handle = win32event.OpenEvent(win32event.EVENT_MODIFY_STATE, False, EVENT_NAME)
    except pywintypes.error as e:
        print(f"Error: Unable to open event ({e})")
        return

    print("Signaling event...")
    win32event.SetEvent(event_handle)

    win32api.CloseHandle(event_handle)

if __name__ == "__main__":
    input("Press Enter to send event... ")
    event_sender()
