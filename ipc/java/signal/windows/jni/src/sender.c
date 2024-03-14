#include <jni.h>
#include <windows.h>

JNIEXPORT jlong JNICALL Java_Sender_openEvent(JNIEnv *env, jobject obj, jstring eventName) {
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
    HANDLE eventHandle = OpenEventW(EVENT_MODIFY_STATE, FALSE, eventNameW);
    free(eventNameW);
    (*env)->ReleaseStringChars(env, eventName, nameChars);
    return (jlong)eventHandle;
}

JNIEXPORT jint JNICALL Java_Sender_setEvent(JNIEnv *env, jobject obj, jlong eventHandle) {
    BOOL result = SetEvent((HANDLE)eventHandle);
    return (jint)result;
}

JNIEXPORT jint JNICALL Java_Sender_closeHandle(JNIEnv *env, jobject obj, jlong eventHandle) {
    BOOL result = CloseHandle((HANDLE)eventHandle);
    return (jint)result;
}
