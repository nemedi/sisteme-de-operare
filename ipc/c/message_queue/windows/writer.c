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
    while (1) {
        printf("Enter a message (type 'quit' to exit): ");
        fgets(message, sizeof(message), stdin);
        size_t len = strlen(message);
        if (len > 0 && message[len - 1] == '\n') {
            message[len - 1] = '\0';
        }
        COPYDATASTRUCT cds;
        cds.dwData = 1;
        cds.cbData = strlen(message) + 1;
        cds.lpData = message;
        SendMessage(receiverWindow, WM_COPYDATA, 0, (LPARAM)&cds);
        if (strcmp(message, "quit") == 0) {
            break;
        }
    }
    return 0;
}
