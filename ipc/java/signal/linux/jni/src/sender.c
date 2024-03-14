#include <jni.h>
#include <signal.h>

JNIEXPORT void JNICALL Java_Sender_kill(JNIEnv *env, jobject obj, jint pid, jint signal) {
    int result = kill((pid_t)pid, (int)signal);
    if (result == -1) {
        perror("Error calling kill");
    }
}
