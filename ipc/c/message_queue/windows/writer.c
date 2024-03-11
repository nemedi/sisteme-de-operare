#include <stdio.h>
#include <windows.h>

#define WM_CUSTOM_MESSAGE (WM_USER + 1)

int main() {
    HWND receiverWindow = FindWindow(NULL, "MessageQueueReceiver");
    if (receiverWindow == NULL) {
        printf("Error: Receiver window not found.\n");
        return 1;
    }
    char message[256];
    printf("Enter a message (type 'quit' to exit): ");
    while (1) {
        fgets(message, sizeof(message), stdin);
        size_t len = strlen(message);
        if (len > 0 && message[len - 1] == '\n') {
            message[len - 1] = '\0';
        }
        PostMessage(receiverWindow, WM_CUSTOM_MESSAGE, 0, (LPARAM)message);
        if (strcmp(message, "quit") == 0) {
            break;
        }
    }
    return 0;
}
