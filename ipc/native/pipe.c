#include <sys/types.h>
#include <sys/stat.h>

int Java_ipc_PipeTool_createPipe(const char* path, int mode) {
    return mkfifo(path, mode);
}
