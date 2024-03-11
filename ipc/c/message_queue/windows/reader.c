#include <stdio.h>
#include <windows.h>

#define WM_CUSTOM_MESSAGE (WM_USER + 1)

LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    if (uMsg == WM_CUSTOM_MESSAGE) {
        char* message = (char*)lParam;
        printf("Received: %s\n", message);
        if (strcmp(message, "quit") == 0) {
            PostQuitMessage(0);
        }
        return 0;
    }
    return DefWindowProc(hwnd, uMsg, wParam, lParam);
}
int main() {
    WNDCLASS windowClass = { 0 };
    windowClass.lpfnWndProc = WindowProc;
    windowClass.hInstance = GetModuleHandle(NULL);
    windowClass.lpszClassName = "MessageQueueReceiver";
    RegisterClass(&windowClass);
    HWND hwnd = CreateWindowEx(0, "MessageQueueReceiver", "MessageQueueReceiver", 0, 0, 0, 0, 0, HWND_MESSAGE, NULL, NULL, NULL);
    if (hwnd == NULL) {
        printf("Error creating receiver window.\n");
        return 1;
    }
    MSG msg;
    while (GetMessage(&msg, NULL, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }
    return 0;
}
