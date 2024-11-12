//
// Created by NU on 2024/11/12.
//

#include "../include/hooDet.h"

// crc32 计算
uint32_t calculateCRC32(const unsigned char *data, size_t length) {
    uint32_t crc = crc32(0L, Z_NULL, 0);
    crc = crc32(crc, data, length);
    return crc;
}

bool openHookStatus() {
    bool isHook = false;
    // 获取 libc open_offset
    SandHook::ElfImg libc("libc.so");
    void *open_addr = libc.getSymbAddress("open");
    uintptr_t open_offset = (uintptr_t) open_addr - (uintptr_t) libc.getBase();
    LOGI("open offset: 0x%x", open_offset);

    // 根据 open_offset 读取本地 libc open 的前16字节进行 CRC32 计算
    int fd = open(libc.name().c_str(), O_RDONLY);
    lseek(fd, open_offset, 0);
    char buf[16];
    read(fd, buf, 16);
    uintptr_t local_crc32_open_value = calculateCRC32(
            reinterpret_cast<const unsigned char *>(buf), 16);

    // 读取内存中的 open 前16字节进行 CRC32 计算
    uintptr_t mem_crc32_open_value = calculateCRC32(
            reinterpret_cast<const unsigned char *>(open_addr), 16);

    LOGI("local open crc32: 0x%x, mem open crc32: 0x%x", local_crc32_open_value,
         mem_crc32_open_value);
    if (local_crc32_open_value != mem_crc32_open_value) {
        isHook = true;
        LOGE("open hook detected");
    }
    close(fd);
    return isHook;
}

bool segmentHookStatus() {
    bool isHook = false;
    // 获取 libc text_offset
    SandHook::ElfImg libc("libc.so");
    auto textInfo = libc.getTextSectionInfo();
    LOGI("text info: 0x%x, 0x%x", textInfo.first, textInfo.second);

    // 根据 text_offset 读取本地 libc text 进行 CRC32 计算
    int fd = open(libc.name().c_str(), O_RDONLY);
    lseek(fd, textInfo.first, 0);
    std::vector<char> buf(textInfo.second);
    read(fd, buf.data(), textInfo.second);
    uintptr_t local_crc32_text_value = calculateCRC32(
            reinterpret_cast<const unsigned char *>(buf.data()), textInfo.second);

    // 读取内存中的 text 进行 CRC32 计算
    uintptr_t text_addr = (uintptr_t) libc.getBase() + textInfo.first;
    uintptr_t mem_crc32_text_value = calculateCRC32(
            reinterpret_cast<const unsigned char *>(text_addr), textInfo.second);

    LOGI("local text crc32: 0x%x, mem text crc32 0x%x", local_crc32_text_value,
         mem_crc32_text_value);

    if (local_crc32_text_value != mem_crc32_text_value) {
        isHook = true;
        LOGE("text hook detected");
    }

    // 获取 libc plt_offset
    auto pltInfo = libc.getPltSectionInfo();
    LOGI("plt info: 0x%x, 0x%x", pltInfo.first, pltInfo.second);

    // 根据 plt_offset 读取本地 libc plt 进行 CRC32 计算
    lseek(fd, pltInfo.first, 0);
    std::vector<char> buf2(pltInfo.second);
    read(fd, buf2.data(), pltInfo.second);
    uintptr_t local_crc32_plt_value = calculateCRC32(
            reinterpret_cast<const unsigned char *>(buf2.data()), pltInfo.second);

    // 读取内存中的 plt 进行 CRC32 计算
    uintptr_t plt_addr = (uintptr_t) libc.getBase() + pltInfo.first;
    uintptr_t mem_crc32_plt_value = calculateCRC32(
            reinterpret_cast<const unsigned char *>(plt_addr), pltInfo.second);

    LOGI("local plt crc32: 0x%x, mem plt crc32 0x%x", local_crc32_plt_value, mem_crc32_plt_value);

    if (local_crc32_plt_value != mem_crc32_plt_value) {
        isHook = true;
        LOGE("plt hook detected");
    }
    close(fd);
    return isHook;
}

bool prettyMethodHookStatus() {
    bool isHook = false;
    SandHook::ElfImg libart("libart.so");
    // 本地文件查找的
    // _ZN3art9ArtMethod12PrettyMethodEb
    // _ZN3art9ArtMethod12PrettyMethodEPS0_b

    // https://github1s.com/LSPosed/LSPlant/blob/master/lsplant/src/main/jni/art/runtime/art_method.cxx#L27
    // lsposed 源码中的，但是在测试中只有第一个可以找到相应的函数，所以还是需要与本地文件相结合来看
    // _ZN3art9ArtMethod12PrettyMethodEPS0_b
    // _ZN3art12PrettyMethodEPNS_9ArtMethodEb
    // _ZN3art12PrettyMethodEPNS_6mirror9ArtMethodEb
    std::array<const char *, 4> symbols = {
            "_ZN3art9ArtMethod12PrettyMethodEb",
            "_ZN3art9ArtMethod12PrettyMethodEPS0_b",
            "_ZN3art12PrettyMethodEPNS_9ArtMethodEb",
            "_ZN3art12PrettyMethodEPNS_6mirror9ArtMethodEb"
    };

    void *PrettyMethod_addr = nullptr;
    for (const auto &symbol: symbols) {
        PrettyMethod_addr = libart.getSymbAddress(symbol);
        if (PrettyMethod_addr != nullptr) {
            break;
        }
    }

    if (PrettyMethod_addr == nullptr) {
        LOGI("PrettyMethod not found");
    }

    uintptr_t PrettyMethod_offset = (uintptr_t) PrettyMethod_addr - (uintptr_t) libart.getBase();
    LOGI("PrettyMethod addr: 0x%x", PrettyMethod_offset);

    // 根据 PrettyMethod_offset 读取本地 libart PrettyMethod 的前16字节进行 CRC32 计算
    int fd = open(libart.name().c_str(), O_RDONLY);
    lseek(fd, PrettyMethod_offset, 0);
    char buf[16];
    read(fd, buf, 16);
    uintptr_t local_crc32_prettymethod_value = calculateCRC32(
            reinterpret_cast<const unsigned char *>(buf), 16);

    // 读取内存中的 PrettyMethod 前16字节进行 CRC32 计算
    uintptr_t mem_crc32_prettymethod_value = calculateCRC32(
            reinterpret_cast<const unsigned char *>(PrettyMethod_addr), 16);

    LOGI("local PrettyMethod crc32: 0x%x, mem PrettyMethod crc32: 0x%x",
         local_crc32_prettymethod_value, mem_crc32_prettymethod_value);
    if (local_crc32_prettymethod_value != mem_crc32_prettymethod_value) {
        isHook = true;
//        LOGE("PrettyMethod hook detected");
    }
    close(fd);
    return isHook;
}

int callStackDetection(JNIEnv *env) {
    // 使用 "../include/obfs-string.h" 的字符串混淆功能
    SandHook::ElfImg libart("libart.so");

    // 获取 libart nativeFillInStackTrace，进而获取 javaStackState
    void *nativeFillInStackTrace = libart.getSymbAddress(
            "_ZN3artL32Throwable_nativeFillInStackTraceEP7_JNIEnvP7_jclass");

    jobject(*my_nativeFillInStackTrace)(JNIEnv * , jclass); // 函数指针
    my_nativeFillInStackTrace = reinterpret_cast<jobject (*)(JNIEnv *,
                                                             jclass)>(nativeFillInStackTrace);
    jclass MainActivity = env->FindClass("com/cztang/riskguard/Activity/HookActivity");
    jobject javaStackState = my_nativeFillInStackTrace(env, MainActivity);

    // 获取 libart nativeGetStackTrace，获取 objAry
    void *nativeGetStackTrace = libart.getSymbAddress(
            "_ZN3artL29Throwable_nativeGetStackTraceEP7_JNIEnvP7_jclassP8_jobject");

    jobjectArray(*my_nativeGetStackTrace)(JNIEnv * , jclass, jobject); // 函数指针
    my_nativeGetStackTrace = reinterpret_cast<jobjectArray (*)(JNIEnv *, jclass,
                                                               jobject)>(nativeGetStackTrace);
    jobjectArray objAry = my_nativeGetStackTrace(env, MainActivity, javaStackState);

    jsize length = env->GetArrayLength(objAry);

    std::list <std::string> myList;
    std::set <std::string> mySet;

    // 遍历 objAry， 获取 className, methodName, fileName, lineNumber
    for (int i = 0; i < length; i++) {
        jobject obj = env->GetObjectArrayElement(objAry, i);
        jclass clazz = env->GetObjectClass(obj);
        jstring className = (jstring) env->CallObjectMethod(
                obj, env->GetMethodID(clazz, "getClassName", "()Ljava/lang/String;"));
        jstring methodName = (jstring) env->CallObjectMethod(
                obj, env->GetMethodID(clazz, "getMethodName", "()Ljava/lang/String;"));
        jstring fileName = (jstring) env->CallObjectMethod(
                obj, env->GetMethodID(clazz, "getFileName", "()Ljava/lang/String;"));

        int lineNumber = env->CallIntMethod(
                obj, env->GetMethodID(clazz, "getLineNumber", "()I"));

        // 如果 fileName 为空，则说明是 lsposed 的 hook
        if (className != nullptr && methodName != nullptr && fileName != nullptr) {
            LOGI("%s -> %s(%s: %d)", env->GetStringUTFChars(className, 0),
                 env->GetStringUTFChars(methodName, 0), env->GetStringUTFChars(fileName, 0),
                 lineNumber);
            char des[128];
            sprintf(des, "%s -> %s(%s: %d)", env->GetStringUTFChars(className, 0),
                    env->GetStringUTFChars(methodName, 0), env->GetStringUTFChars(fileName, 0),
                    lineNumber);
            myList.push_back(des);
            mySet.insert(des);
        } else {
            LOGE("xposed hook detected");
            return 2;
        }
    }

    // 如果存在重复的元素，说明可能是 frida hook
    if (myList.size() == mySet.size()) {
        std::string last = myList.back();
        // 如果函数调用不是从默认方法开始调用的，则说明是 rpc hook(比如使用 frida 远程调用函数)
        if (strstr(last.c_str(), "com.android.internal.os.ZygoteInit -> main") ||
            strstr(last.c_str(), "java.lang.Thread -> run")) {
            LOGI("all is right");
            return 0;
        } else {
            LOGE("rpc hook detected");
            return 1;
        }
    } else {
        LOGE("frida hook detected");
        return 3;
    }
}