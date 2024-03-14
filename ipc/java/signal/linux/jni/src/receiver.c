#include <jni.h>
#include <stdio.h>
#include <signal.h>

JavaVM *jvm;

void signalHandler(int signal) {
    JNIEnv *env;
    (*jvm)->AttachCurrentThread(jvm, (void **)&env, NULL);
    jclass clazz = (*env)->FindClass(env, "Receiver");
    if (clazz == NULL) {
        printf("Receiver class not found.\n");
        return;
    }
    jmethodID methodID = (*env)->GetMethodID(env, clazz, "handleSignal", "()V");
    if (methodID == NULL) {
        printf("handleSignal method not found.\n");
        return;
    }
    (*env)->CallVoidMethod(env, NULL, methodID);
}
JNIEXPORT void JNICALL Java_Receiver_registerSignalHandler(JNIEnv *env, jobject obj, jint sig) {
    (*env)->GetJavaVM(env, &jvm);
    signal(sig, signalHandler);
}
