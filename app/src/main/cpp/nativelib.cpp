#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_app_demo_framework_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject obj) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}