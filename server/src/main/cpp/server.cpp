#include <jni.h>
#include <unistd.h>
#include <iostream>
#include <android/log.h>

using namespace std;

#define LOG_TAG "app.jhau.appopsmanager-JNI"
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN,  LOG_TAG, __VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__)

#define PRINT(s) std::cout << s << std::endl;

extern "C"
JNIEXPORT void JNICALL
Java_app_jhau_server_ServerStarter_startServerNative(JNIEnv *env, jclass clazz, jstring apk_path) {
    PRINT("Start server.")
    int pid = daemon(false, false);
    if (pid < 0) PRINT("Fork server failed.")
    if (pid == 0) {
        string classPath = "-Djava.class.path=" + string(env->GetStringUTFChars(apk_path, 0));
        int execCode = execl(
                "/system/bin/app_process",
                "app_process",
                classPath.c_str(),
                "/system/bin",
                "--nice-name=appops_server",
                "app.jhau.server.AppOpsServer",
                nullptr
        );
        if (execCode) PRINT("Server start failed.")
    }
}

void get_cmdline_pid(pid_t pid, char *name) {
    char proc_path[20];
    sprintf(proc_path, "/proc/%d/cmdline", pid);
    FILE *f = fopen(proc_path, "r");
    if (f == nullptr) {
        return;
    }
    fscanf(f, "%[^\n]", name);
    fclose(f);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_app_jhau_server_AppOpsServer_00024IServerThread_getCmdlineByPid(JNIEnv *env, jobject thiz,
                                                                     jint pid) {
    char name[1024];
    get_cmdline_pid(pid, name);
    return env->NewStringUTF(name);
}