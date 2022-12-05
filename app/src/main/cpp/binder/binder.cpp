//
// Created by chen on 2022/12/5.
//

#include "binder.h"

#define LOG_TAG "Binder"

#include <errno.h>
#include <fcntl.h>
#include <inttypes.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <unistd.h>
#include "android/log.h"

#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN,  LOG_TAG, __VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__)


#define NAME(n) case n: return #n

const char *cmd_name(uint32_t cmd) {
    switch (cmd) {
        NAME(BR_NOOP);
        NAME(BR_TRANSACTION_COMPLETE);
        NAME(BR_INCREFS);
        NAME(BR_ACQUIRE);
        NAME(BR_RELEASE);
        NAME(BR_DECREFS);
        NAME(BR_TRANSACTION);
        NAME(BR_REPLY);
        NAME(BR_FAILED_REPLY);
        NAME(BR_DEAD_REPLY);
        NAME(BR_DEAD_BINDER);
        default:
            return "???";
    }
}

#define hexdump(a, b) do{} while (0)
#define binder_dump_txn(txn)  do{} while (0)

static void *bio_get(struct binder_io *bio, size_t size);
void bio_put_obj(struct binder_io *bio, void *ptr);
static struct flat_binder_object *bio_alloc_obj(struct binder_io *bio);
static void *bio_alloc(struct binder_io *bio, size_t size);
void bio_put_string16_x(struct binder_io *bio, const char *_str);
void bio_put_uint32(struct binder_io *bio, uint32_t n);

struct binder_state *binder_open(const char *driver, size_t mapsize) {
    struct binder_state *bs;
    struct binder_version vers;

    bs = static_cast<binder_state *>(malloc(sizeof(*bs)));
    if (!bs) {
        errno = ENOMEM;
        return NULL;
    }

    bs->fd = open(driver, O_RDWR | O_CLOEXEC);
    if (bs->fd < 0) {
        fprintf(stderr, "binder: cannot open %s (%s)\n",
                driver, strerror(errno));
        goto fail_open;
    }

    if ((ioctl(bs->fd, BINDER_VERSION, &vers) == -1) ||
        (vers.protocol_version != BINDER_CURRENT_PROTOCOL_VERSION)) {
        fprintf(stderr,
                "binder: kernel driver version (%d) differs from user space version (%d)\n",
                vers.protocol_version, BINDER_CURRENT_PROTOCOL_VERSION);
        goto fail_open;
    }

    bs->mapsize = mapsize;
    bs->mapped = mmap(NULL, mapsize, PROT_READ, MAP_PRIVATE, bs->fd, 0);
    if (bs->mapped == MAP_FAILED) {
        fprintf(stderr, "binder: cannot map device (%s)\n",
                strerror(errno));
        goto fail_map;
    }

    return bs;

    fail_map:
    close(bs->fd);
    fail_open:
    free(bs);
    return NULL;
}

void bio_init_from_txn(struct binder_io *bio, struct binder_transaction_data *txn)
{
    bio->data = bio->data0 = (char *)(intptr_t)txn->data.ptr.buffer;
    bio->offs = bio->offs0 = (binder_size_t *)(intptr_t)txn->data.ptr.offsets;
    bio->data_avail = txn->data_size;
    bio->offs_avail = txn->offsets_size / sizeof(size_t);
    bio->flags = BIO_F_SHARED;
}

int binder_write(struct binder_state *bs, void *data, size_t len)
{
    struct binder_write_read bwr;
    int res;

    bwr.write_size = len;
    bwr.write_consumed = 0;
    bwr.write_buffer = (uintptr_t) data;
    bwr.read_size = 0;
    bwr.read_consumed = 0;
    bwr.read_buffer = 0;
    res = ioctl(bs->fd, BINDER_WRITE_READ, &bwr);
    if (res < 0) {
        fprintf(stderr,"binder_write: ioctl failed (%s)\n",
                strerror(errno));
    }
    return res;
}

void binder_free_buffer(struct binder_state *bs,
                        binder_uintptr_t buffer_to_free)
{
    struct {
        uint32_t cmd_free;
        binder_uintptr_t buffer;
    } __attribute__((packed)) data;
    data.cmd_free = BC_FREE_BUFFER;
    data.buffer = buffer_to_free;
    binder_write(bs, &data, sizeof(data));
}

void binder_send_reply(struct binder_state *bs,
                       struct binder_io *reply,
                       binder_uintptr_t buffer_to_free,
                       int status)
{
    struct {
        uint32_t cmd_free;
        binder_uintptr_t buffer;
        uint32_t cmd_reply;
        struct binder_transaction_data txn;
    } __attribute__((packed)) data;

    data.cmd_free = BC_FREE_BUFFER;
    data.buffer = buffer_to_free;
    data.cmd_reply = BC_REPLY;
    data.txn.target.ptr = 0;
    data.txn.cookie = 0;
    data.txn.code = 0;
    if (status) {
        data.txn.flags = TF_STATUS_CODE;
        data.txn.data_size = sizeof(int);
        data.txn.offsets_size = 0;
        data.txn.data.ptr.buffer = (uintptr_t)&status;
        data.txn.data.ptr.offsets = 0;
    } else {
        data.txn.flags = 0;
        data.txn.data_size = reply->data - reply->data0;
        data.txn.offsets_size = ((char*) reply->offs) - ((char*) reply->offs0);
        data.txn.data.ptr.buffer = (uintptr_t)reply->data0;
        data.txn.data.ptr.offsets = (uintptr_t)reply->offs0;
    }
    binder_write(bs, &data, sizeof(data));
}

void bio_init(struct binder_io *bio, void *data,
              size_t maxdata, size_t maxoffs)
{
    size_t n = maxoffs * sizeof(size_t);

    if (n > maxdata) {
        bio->flags = BIO_F_OVERFLOW;
        bio->data_avail = 0;
        bio->offs_avail = 0;
        return;
    }

    bio->data = bio->data0 = (char *) data + n;
    bio->offs = bio->offs0 = static_cast<binder_size_t *>(data);
    bio->data_avail = maxdata - n;
    bio->offs_avail = maxoffs;
    bio->flags = 0;
}

int binder_parse(struct binder_state *bs, struct binder_io *bio, uintptr_t ptr, size_t size,
                 binder_handler func) {
    int r = 1;
    uintptr_t end = ptr + (uintptr_t) size;

    while (ptr < end) {
        uint32_t cmd = *(uint32_t *) ptr;
        ptr += sizeof(uint32_t);
#if TRACE
        fprintf(stderr,"%s:\n", cmd_name(cmd));
#endif
        switch (cmd) {
            case BR_NOOP:
                break;
            case BR_TRANSACTION_COMPLETE:
                break;
            case BR_INCREFS:
            case BR_ACQUIRE:
            case BR_RELEASE:
            case BR_DECREFS:
#if TRACE
                fprintf(stderr,"  %p, %p\n", (void *)ptr, (void *)(ptr + sizeof(void *)));
#endif
                ptr += sizeof(struct binder_ptr_cookie);
                break;
            case BR_TRANSACTION_SEC_CTX:
            case BR_TRANSACTION: {
                struct binder_transaction_data_secctx txn;
                if (cmd == BR_TRANSACTION_SEC_CTX) {
                    if ((end - ptr) < sizeof(struct binder_transaction_data_secctx)) {
                        ALOGE("parse: txn too small (binder_transaction_data_secctx)!\n");
                        return -1;
                    }
                    memcpy(&txn, (void *) ptr, sizeof(struct binder_transaction_data_secctx));
                    ptr += sizeof(struct binder_transaction_data_secctx);
                } else /* BR_TRANSACTION */ {
                    if ((end - ptr) < sizeof(struct binder_transaction_data)) {
                        ALOGE("parse: txn too small (binder_transaction_data)!\n");
                        return -1;
                    }
                    memcpy(&txn.transaction_data, (void *) ptr,
                           sizeof(struct binder_transaction_data));
                    ptr += sizeof(struct binder_transaction_data);

                    txn.secctx = 0;
                }

                binder_dump_txn(&txn.transaction_data);
                if (func) {
                    unsigned rdata[256 / 4];
                    struct binder_io msg;
                    struct binder_io reply;
                    int res;

                    bio_init(&reply, rdata, sizeof(rdata), 4);
                    bio_init_from_txn(&msg, &txn.transaction_data);
                    res = func(bs, &txn, &msg, &reply);
                    if (txn.transaction_data.flags & TF_ONE_WAY) {
                        binder_free_buffer(bs, txn.transaction_data.data.ptr.buffer);
                    } else {
                        binder_send_reply(bs, &reply, txn.transaction_data.data.ptr.buffer, res);
                    }
                }
                break;
            }
            case BR_REPLY: {
                struct binder_transaction_data *txn = (struct binder_transaction_data *) ptr;
                if ((end - ptr) < sizeof(*txn)) {
                    ALOGE("parse: reply too small!\n");
                    return -1;
                }
                binder_dump_txn(txn);
                if (bio) {
                    bio_init_from_txn(bio, txn);
                    bio = 0;
                } else {
                    /* todo FREE BUFFER */
                }
                ptr += sizeof(*txn);
                r = 0;
                break;
            }
            case BR_DEAD_BINDER: {
                struct binder_death *death = (struct binder_death *) (uintptr_t) *(binder_uintptr_t *) ptr;
                ptr += sizeof(binder_uintptr_t);
                death->func(bs, death->ptr);
                break;
            }
            case BR_FAILED_REPLY:
                r = -1;
                break;
            case BR_DEAD_REPLY:
                r = -1;
                break;
            default:
                ALOGE("parse: OOPS %d\n", cmd);
                return -1;
        }
    }

    return r;
}

int binder_call(struct binder_state *bs,
                struct binder_io *msg, struct binder_io *reply,
                uint32_t target, uint32_t code) {
    int res;
    struct binder_write_read bwr;
    struct {
        uint32_t cmd;
        struct binder_transaction_data txn;
    } __attribute__((packed)) writebuf;
    unsigned readbuf[32];

    if (msg->flags & BIO_F_OVERFLOW) {
        fprintf(stderr, "binder: txn buffer overflow\n");
        goto fail;
    }

    writebuf.cmd = BC_TRANSACTION;
    writebuf.txn.target.handle = target;
    writebuf.txn.code = code;
    writebuf.txn.flags = 0;
    writebuf.txn.data_size = msg->data - msg->data0;
    writebuf.txn.offsets_size = ((char *) msg->offs) - ((char *) msg->offs0);
    writebuf.txn.data.ptr.buffer = (uintptr_t) msg->data0;
    writebuf.txn.data.ptr.offsets = (uintptr_t) msg->offs0;

    bwr.write_size = sizeof(writebuf);
    bwr.write_consumed = 0;
    bwr.write_buffer = (uintptr_t) &writebuf;

    hexdump(msg->data0, msg->data - msg->data0);
    for (;;) {
        bwr.read_size = sizeof(readbuf);
        bwr.read_consumed = 0;
        bwr.read_buffer = (uintptr_t) readbuf;

        res = ioctl(bs->fd, BINDER_WRITE_READ, &bwr);

        if (res < 0) {
            fprintf(stderr, "binder: ioctl failed (%s)\n", strerror(errno));
            goto fail;
        }

        res = binder_parse(bs, reply, (uintptr_t) readbuf, bwr.read_consumed, 0);
        if (res == 0) return 0;
        if (res < 0) goto fail;
    }

    fail:
    memset(reply, 0, sizeof(*reply));
    reply->flags |= BIO_F_IOERROR;
    return -1;
}

void binder_done(struct binder_state *bs,
                 __unused struct binder_io *msg,
                 struct binder_io *reply)
{
    struct {
        uint32_t cmd;
        uintptr_t buffer;
    } __attribute__((packed)) data;

    if (reply->flags & BIO_F_SHARED) {
        data.cmd = BC_FREE_BUFFER;
        data.buffer = (uintptr_t) reply->data0;
        binder_write(bs, &data, sizeof(data));
        reply->flags = 0;
    }
}

uint32_t bio_get_uint32(struct binder_io *bio)
{
    uint32_t *ptr = (uint32_t *)bio_get(bio, sizeof(*ptr));
    return ptr ? *ptr : 0;
}

static void *bio_get(struct binder_io *bio, size_t size)
{
    size = (size + 3) & (~3);

    if (bio->data_avail < size){
        bio->data_avail = 0;
        bio->flags |= BIO_F_OVERFLOW;
        return NULL;
    }  else {
        void *ptr = bio->data;
        bio->data += size;
        bio->data_avail -= size;
        return ptr;
    }
}

void bio_put_obj(struct binder_io *bio, void *ptr)
{
    struct flat_binder_object *obj;

    obj = bio_alloc_obj(bio);
    if (!obj)
        return;

    obj->flags = 0x7f | FLAT_BINDER_FLAG_ACCEPTS_FDS;
    obj->hdr.type = BINDER_TYPE_BINDER;
    obj->binder = (uintptr_t)ptr;
    obj->cookie = 0;
}

static struct flat_binder_object *bio_alloc_obj(struct binder_io *bio)
{
    struct flat_binder_object *obj;

    obj = static_cast<flat_binder_object *>(bio_alloc(bio, sizeof(*obj)));

    if (obj && bio->offs_avail) {
        bio->offs_avail--;
        *bio->offs++ = ((char*) obj) - ((char*) bio->data0);
        return obj;
    }

    bio->flags |= BIO_F_OVERFLOW;
    return NULL;
}

static void *bio_alloc(struct binder_io *bio, size_t size)
{
    size = (size + 3) & (~3);
    if (size > bio->data_avail) {
        bio->flags |= BIO_F_OVERFLOW;
        return NULL;
    } else {
        void *ptr = bio->data;
        bio->data += size;
        bio->data_avail -= size;
        return ptr;
    }
}

void bio_put_string16_x(struct binder_io *bio, const char *_str)
{
    unsigned char *str = (unsigned char*) _str;
    size_t len;
    uint16_t *ptr;

    if (!str) {
        bio_put_uint32(bio, 0xffffffff);
        return;
    }

    len = strlen(_str);

    if (len >= (MAX_BIO_SIZE / sizeof(uint16_t))) {
        bio_put_uint32(bio, 0xffffffff);
        return;
    }

    /* Note: The payload will carry 32bit size instead of size_t */
    bio_put_uint32(bio, len);
    ptr = static_cast<uint16_t *>(bio_alloc(bio, (len + 1) * sizeof(uint16_t)));
    if (!ptr)
        return;

    while (*str)
        *ptr++ = *str++;
    *ptr++ = 0;
}

void bio_put_uint32(struct binder_io *bio, uint32_t n)
{
    uint32_t *ptr = static_cast<uint32_t *>(bio_alloc(bio, sizeof(n)));
    if (ptr)
        *ptr = n;
}