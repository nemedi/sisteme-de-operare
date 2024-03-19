#define _GNU_SOURCE
#include <sys/resource.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char* argv[])
{
    if (argc == 3)
    {
        int pid = atoi(argv[1]);
        int priority = getpriority(PRIO_PROCESS, pid);
        if (priority == -1) {
            perror("getpriority");
            exit(EXIT_FAILURE);
        }
        printf("Current priority of process %d: %d\n", pid, priority);
        int new_priority = atoi(argv[2]);
        if (setpriority(PRIO_PROCESS, pid, new_priority) == -1)
        {
            perror("setpriority");
            exit(EXIT_FAILURE);
        }
        priority = getpriority(PRIO_PROCESS, pid);
        if (priority == -1)
        {
            perror("getpriority");
            exit(EXIT_FAILURE);
        }
        printf("New priority of process %d: %d\n", pid, priority);
        return 0;
    }
    else
    {
        return 1;
    }
}
