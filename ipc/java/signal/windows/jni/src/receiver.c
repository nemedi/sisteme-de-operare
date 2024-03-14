#include <jni.h>
#include <windows.h>

JNIEXPORT jlong JNICALL Java_Receiver_createEvent(JNIEnv *env, jobject obj, jboolean manualReset, jboolean initialState, jstring eventName) {
    const jchar *nameChars = (*env)->GetStringChars(env, eventName, NULL);
    jsize nameLen = (*env)->GetStringLength(env, eventName);
    wchar_t *eventNameW = (wchar_t *)calloc(nameLen + 1, sizeof(wchar_t));
    if (eventNameW == NULL) {
        return 0;
    }
    for (int i = 0; i < nameLen; ++i) {
        eventNameW[i] = (wchar_t)nameChars[i];
    }
    eventNameW[nameLen] = L'\0';
    HANDLE eventHandle = CreateEventW(NULL, manualReset ? TRUE : FALSE, initialState ? TRUE : FALSE, eventNameW);
    free(eventNameW);
    (*env)->ReleaseStringChars(env, eventName, nameChars);
    return (jlong)eventHandle;
}

JNIEXPORT jint JNICALL Java_Receiver_waitForSingleObject(JNIEnv *env, jobject obj, jlong eventHandle, jint milliseconds) {
    DWORD result = WaitForSingleObject((HANDLE)eventHandle, (DWORD)milliseconds);
    return (jint)result;
}

JNIEXPORT jint JNICALL Java_Receiver_closeHandle(JNIEnv *env, jobject obj, jlong eventHandle) {
    BOOL result = CloseHandle((HANDLE)eventHandle);
    return (jint)result;
}
