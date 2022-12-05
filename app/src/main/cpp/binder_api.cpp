
#include <jni.h>
#include <string>
#include <jni.h>
#include "android/log.h"
#include "binder/binder.h"

int publishService(binder_state *pState, jstring name, void *handler);

int handler(struct binder_state *bs,
             struct binder_transaction_data_secctx *txn_secctx,
             struct binder_io *msg,
             struct binder_io *reply);

extern "C" {

}

extern "C"
JNIEXPORT jint JNICALL
Java_app_demo_framework_service_ShellService_addService(JNIEnv *env, jclass clazz,
                                                        jstring service_name) {
    struct binder_state *bs;
    char *driver;

    driver = "/dev/binder";

    bs = binder_open(driver, 128 * 1024);
    if (!bs) {
        return -1;
    } else {
        //注册Binder服务
        if (publishService(bs, service_name, (void *)handler)) {
            return -1;
        }
    }
    __android_log_print(ANDROID_LOG_INFO, "binder_jni", "success");
    return 0;
}

int handler(struct binder_state *bs,
            struct binder_transaction_data_secctx *txn_secctx,
            struct binder_io *msg,
            struct binder_io *reply) {

    return -1;
}

int publishService(binder_state *bs, jstring name, void *handler) {

    int status;
    unsigned iodata[512 / 4];
    uint32_t svcmgr = BINDER_SERVICE_MANAGER;
    struct binder_io msg, reply;

    bio_init(&msg, iodata, sizeof(iodata), 4);
    bio_put_uint32(&msg, 0);  // strict mode header
    bio_put_uint32(&msg, 0);
    bio_put_string16_x(&msg, SVC_MGR_NAME);
    bio_put_string16_x(&msg, (const char *)name);
    bio_put_obj(&msg, handler);
    bio_put_uint32(&msg, 0);
    bio_put_uint32(&msg, 0);

    if (binder_call(bs, &msg, &reply, svcmgr, SVC_MGR_ADD_SERVICE)) {
        fprintf(stderr, "svcmgr_public 远程调用失败\n");
        return -1;
    }

    status = bio_get_uint32(&reply); //调用成功返回0
    binder_done(bs, &msg, &reply);

    return status;
}

extern "C"
JNIEXPORT jint JNICALL
Java_app_demo_framework_MainActivity_addService(JNIEnv *env, jobject thiz,
                                                jstring service_name) {
    struct binder_state *bs;
    char *driver;

    driver = "/dev/binder";

    bs = binder_open(driver, 128 * 1024);
    if (!bs) {
        return -1;
    } else {
        //注册Binder服务
        if (publishService(bs, service_name, (void *)handler)) {
            return -1;
        }
    }
    __android_log_print(ANDROID_LOG_INFO, "binder_jni", "success");
    return 0;
}