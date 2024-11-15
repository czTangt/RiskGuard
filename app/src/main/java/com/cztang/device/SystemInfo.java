package com.cztang.device;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import com.cztang.riskguard.Activity.DeviceActivity;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SystemInfo extends Device {

    public SystemInfo(DeviceActivity deviceActivity) {
        super(deviceActivity);
    }

    public String getBuildInfo() {
        StringBuilder infoBuilder = new StringBuilder();

        // Device Model
        infoBuilder.append("Model: ").append(Build.MODEL).append("\n");
        // Device Brand
        infoBuilder.append("Brand: ").append(Build.BRAND).append("\n");
        // Android SDK Version
        infoBuilder.append("SDK Version: ").append(Build.VERSION.SDK_INT).append("\n");
        // Device Fingerprint
        infoBuilder.append("Fingerprint: ").append(Build.FINGERPRINT);

        String buildInfo = infoBuilder.toString();
        Log.i(TAG, "BuildInfo: " + "\n" + buildInfo);
        return buildInfo;
    }

    // 获取基带版本，和 Build.getRadioVersion() 方法效果相同，但后者在 API 24 及以上版本可用
    // 基带版本信息指的是手机调制解调器（也称为基带处理器或基带芯片）所使用的固件或驱动程序的版本号。
    // 调制解调器负责处理无线通信信号，使手机能够进行语音通话、发送短信以及连接到移动数据网络。
    public String getBaseband() {
        String retval = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                @SuppressLint("PrivateApi")
                Class<?> clazz = Class.forName("android.os.SystemProperties");
                Object invoker = HiddenApiBypass.newInstance(clazz);
                Object result = HiddenApiBypass.invoke(clazz, invoker, "get", "gsm.version.baseband", "no message");
                retval = (String) result;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                @SuppressLint("PrivateApi")
                Class<?> clazz = Class.forName("android.os.SystemProperties");
                Object invoker = clazz.newInstance();
                Method get = clazz.getDeclaredMethod("get", String.class, String.class);
                Object result = get.invoke(invoker, "gsm.version.baseband", "no message");
                assert result != null;
                retval = (String) result;
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "BasebandInfo: " + retval);
        return "BasebandInfo: " + retval;
    }

}
