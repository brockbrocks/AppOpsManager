#include <jni.h>
#include <cstdio>
#include <unistd.h>

extern "C"
JNIEXPORT jint JNICALL
Java_app_jhau_server_ServerStarter_fork(JNIEnv *env, jclass clazz) {
    pid_t pid = fork();
    return pid;
}
extern "C"
JNIEXPORT jint JNICALL
Java_app_jhau_server_ServerStarter_add(JNIEnv *env, jclass clazz, jint a, jint b) {
    return a + b;
}