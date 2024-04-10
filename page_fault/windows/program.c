#include <stdio.h>
#include <windows.h>

LONG WINAPI PageFaultExceptionHandler(PEXCEPTION_POINTERS exceptionInfo) {
    printf("Page fault occurred!\n");
    return EXCEPTION_EXECUTE_HANDLER;
}

int main() {
    if (!SetUnhandledExceptionFilter(PageFaultExceptionHandler)) {
        fprintf(stderr, "Failed to set up exception handler!\n");
        return 1;
    }
    int *ptr = NULL;
    *ptr = 10;
    return 0;
}
