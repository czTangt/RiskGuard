//
// Created by NU on 2024/11/12.
//

#ifndef RISKGUARD_HOODET_H
#define RISKGUARD_HOODET_H

#pragma once
#include "../include/elf_util.h"
#include "../include/config.h"
#include <stdint.h>
#include <zlib.h>
#include <vector>
#include <jni.h>
#include <list>
#include <set>

// 通过 CRC32 比较内存和本地文件数据比较 open 处是否被修改
bool openHookStatus();

// 通过 CRC32 比较内存和本地文件数据比较 .text 和 .plt段 是否被修改
bool segmentHookStatus();

// 通过 CRC32 比较内存和本地文件数据比较 PrettyMethod 是否被修改
bool prettyMethodHookStatus();

// 利用 java 层调用栈检测 hook 工具存在状态，主要是解析 StackTraceElement[] 这个数组来进行检测
int callStackDetection(JNIEnv *env);


#endif //RISKGUARD_HOODET_H
