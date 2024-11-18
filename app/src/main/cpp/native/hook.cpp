//
// Created by NU on 2024/11/16.
//

#include "../include/hookDet.h"
#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_cztang_hook_Hook_getHookInfo(JNIEnv *env, jobject thiz) {
    std::string hookInfo;
    if (openHookStatus()) {
        hookInfo += "open func hook detected\n";
    }

    if (segmentHookStatus()) {
        hookInfo += "segment hook detected\n";
    }

    if (prettyMethodHookStatus()) {
        hookInfo += "PrettyMethod hook detected\n";
    }

    int callStack = callStackDetection(env);
    if (callStack) {
        hookInfo += "callStackDetection: \n";
        switch (callStackDetection(env)) {
            case 0:
                hookInfo += "No hook tool detected\n";
                break;
            case 1:
                hookInfo += "rpc hook detected\n";
                break;
            case 2:
                hookInfo += "xposed hook detected\n";
                break;
            case 3:
                hookInfo += "frida hook detected\n";
                break;
        }
    }
    if (hookInfo.empty()) {
        hookInfo = "No hook trace detected";
    }
    return env->NewStringUTF(hookInfo.c_str());
}