//
// Created by NU on 2024/11/11.
//

#include "include/deviceDet.h"
#include <jni.h>
#include <sys/utsname.h>

// 获取 HardDisk 信息
extern "C"
JNIEXPORT jstring JNICALL
Java_com_cztang_device_Device_getHardDiskInfor(JNIEnv *env, jobject thiz) {
    // 获取 StatFs 类
    jclass statFsClass = env->FindClass("android/os/StatFs");
    jmethodID id = env->GetMethodID(statFsClass, "<init>", "(Ljava/lang/String;)V");
    jobject statFsObj = env->NewObject(statFsClass, id, env->NewStringUTF("/storage/emulated/0"));

    // 获取 total bytes
    jlong totalBytes = env->CallLongMethod(statFsObj,
                                           env->GetMethodID(statFsClass, "getTotalBytes", "()J"));

    // 使用 statfs64 获取更多信息
    struct statfs64 buf{};
    if (statfs64("/storage/emulated/0", &buf) == -1) {
        LOGE("statfs64 System Information Failed: %s", strerror(errno));
        return env->NewStringUTF("Error: Failed to get filesystem information.");
    }

    char info[4096];
    snprintf(info, sizeof(info),
             "Total Bytes: 0x%llx\n"
             "Filesystem Type: 0x%x\n"
             "Block Size: 0x%x\n"
             "Total Blocks: 0x%llx\n"
             "Free Blocks: 0x%llx\n"
             "Available Blocks: 0x%llx\n"
             "Total Inodes: 0x%llx\n"
             "Free Inodes: 0x%llx\n"
             "Filesystem ID: 0x%llx\n"
             "Maximum Filename Length: 0x%x",
             totalBytes, // Total Bytes
             buf.f_type, // Filesystem Type
             buf.f_bsize, // Block Size
             buf.f_blocks, // Total Blocks
             buf.f_bfree, // Free Blocks
             buf.f_bavail, // Available Blocks
             buf.f_files, // Total Inodes
             buf.f_ffree, // Free Inodes
             ((unsigned long long) buf.f_fsid.__val[0] << 32) |
             buf.f_fsid.__val[1], // Filesystem ID
             buf.f_namelen); // Maximum Filename Length

    LOGI("Disk Info: %s", info);
    return env->NewStringUTF(info);
}

// 获取 Kernel 信息
extern "C"
JNIEXPORT jstring JNICALL
Java_com_cztang_device_Device_getKernelInfor(JNIEnv *env, jobject thiz) {
    struct utsname uts{};
    if(uname(&uts) != -1){
        char info[4096];
        snprintf(info, sizeof(info),
                 "System Name: %s\n"
                 "Node Name: %s\n"
                 "Release: %s\n"
                 "Version: %s\n"
                 "Machine: %s\n"
                 "Domain Name: %s",
                 uts.sysname, // System Name
                 uts.nodename, // Node Name
                 uts.release, // Release
                 uts.version, // Version
                 uts.machine, // Machine
                 uts.domainname); // Domain Name

        LOGI("Kernel Info\n%s", info);
        return env->NewStringUTF(info);
    } else {
        LOGE("uname Kernel Information Failed: %s", strerror(errno));
        return env->NewStringUTF("Error: Failed to get kernel information.");
    }
}