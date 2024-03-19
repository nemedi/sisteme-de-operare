#include <windows.h>
#include <stdio.h>
#include <string.h>

DWORD parsePriority(char* szPriority) {
    if (_stricmp(szPriority, "IDLE") == 0)
    {
        return IDLE_PRIORITY_CLASS;
    }
    else if (_stricmp(szPriority, "BELOW_NORMAL") == 0)
    {
        return BELOW_NORMAL_PRIORITY_CLASS;
    }
    else if (_stricmp(szPriority, "NORMAL") == 0)
    {
        return NORMAL_PRIORITY_CLASS;
    }
    else if (_stricmp(szPriority, "ABOVE_NORMAL") == 0)
    {
        return ABOVE_NORMAL_PRIORITY_CLASS;
    }
    else  if (_stricmp(szPriority, "HIGH") == 0)
    {
         return HIGH_PRIORITY_CLASS;
    }
    else if (_stricmp(szPriority, "REALTIME") == 0)
    {
        return REALTIME_PRIORITY_CLASS;
    }
    else
    {
        return NORMAL_PRIORITY_CLASS;
    }
}

int main(int argc, char* argv[])
{
    if (argc == 3)
    {
        DWORD pid = strtoul(argv[2], NULL, 10);
        DWORD priority = parsePriority(argv[3]);
        HANDLE hProcess = OpenProcess(PROCESS_ALL_ACCESS, FALSE, pid);
        if (hProcess != NULL)
        {
            BOOL success = SetPriorityClass(hProcess, priority);
            if (success)
            {
                printf("Process priority set successfully.\n");
            }
            else
            {
                printf("Failed to set process priority.\n");
            }
            CloseHandle(hProcess);
        }
        else
        {
            printf("Failed to open process handle.\n");
        }
        return 0;
    }
    else 
    {
        return 1;
    }
}