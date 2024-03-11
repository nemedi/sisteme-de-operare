#include <stdio.h>
#include <windows.h>

int main() {
    HANDLE hSemaphore;
    const char* SEMAPHORE_NAME = "MyNamedSemaphore";
    // Create a named semaphore with an initial count of 1
    hSemaphore = CreateSemaphore(NULL, 1, 1, SEMAPHORE_NAME);

    if (hSemaphore == NULL) {
        perror("Error creating named semaphore");
        return 1;
    }

    printf("Named semaphore created successfully.\n");

    // Use the named semaphore as needed...

    // Close the named semaphore handle when done
    CloseHandle(hSemaphore);

    return 0;
}
