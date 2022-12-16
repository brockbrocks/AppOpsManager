#include <jni.h>
#include <unistd.h>
#include <iostream>
#include <android/log.h>

#define LOG_TAG "app.jhau.appopsmanager-JNI"
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN,  LOG_TAG, __VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__)

#define PRINT(s) std::cout << s << std::endl;

extern "C"
JNIEXPORT void JNICALL
Java_app_jhau_server_ServerStarter_startServerNative(JNIEnv *env, jclass clazz) {
    PRINT("Start server.")
    int pid = daemon(false, false);
    if (pid < 0) PRINT("Fork server failed.")
    if (pid == 0) {
        int execCode = execl(
                "/system/bin/app_process",
                "app_process",
                "/system/bin",
                "--nice-name=appops_server",
                "app.jhau.server.AppOpsServer",
                nullptr
        );
        if (execCode) PRINT("Server start failed.")
    }
}