package com.cztang.device;

import android.os.Build;
import android.util.Log;

import com.cztang.riskguard.Activity.DeviceActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class RootInfo extends Device {

    // 敏感路径，里面若有特殊的文件可视为root
    public static final String[] targetPaths = {
            "/data/local/",
            "/data/local/bin/",
            "/data/local/xbin/",
            "/sbin/",
            "/su/bin/",
            "/system/bin/",
            "/system/bin/.ext/",
            "/system/bin/failsafe/",
            "/system/sd/xbin/",
            "/system/usr/we-need-root/",
            "/system/xbin/",
            "/cache/",
            "/data/",
            "/dev/"
    };

    public static final String[] fileNames = {
            "su",
            "magisk",
            "busybox"
    };

    // 检测本应该不可读写的路径是否有读写权限
    private String[] NotWritePermissionPaths = {
            "/system",
            "/system/bin",
            "/system/sbin",
            "/system/xbin",
            "/vendor/bin",
            "/sbin",
            "/etc"
//           这里从 vivo 手机上测试发现这些路径都是有读写权限的
//            "/sys",
//            "/proc",
//            "/dev"
    };

    public RootInfo(DeviceActivity deviceActivity) {
        super(deviceActivity);
    }

    public String getRootInfo() {
        if (getBuildInfo() || getSuInfo() || getFileInfo() || getPermissionInfo()) {
            return "This device is rooted";
        }

        return "This device is not rooted";
    }

    private boolean getBuildInfo() {
        return Build.TAGS.equals("test-keys") || Build.FINGERPRINT.contains("userdebug");
    }

    private boolean getSuInfo() {
        try {
            // 执行 which su
            Process whichSu = Runtime.getRuntime().exec(new String[]{"which", "su"});
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(whichSu.getInputStream()));
            String suPath = bufferedReader.readLine();

            // 检查 su 路径是否存在且为有效路径
            return suPath != null && (suPath.startsWith("/sbin/su") || suPath.startsWith("/system/xbin/su"));
        } catch (IOException e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : "Error executing 'which su'");
            return false;
        }
    }

    private boolean getFileInfo() {
        // 获取可能的文件路径列表
        String[] pathsArray = this.getPaths();

        // 遍历所有路径, 检查可疑文件是否在这些路径中存在
        for (String path : pathsArray) {
            for (String filename : fileNames) {
                File f = new File(path, filename);
                boolean fileExists = f.exists(); // 检查文件是否存在,实际调用的是系统调用 faccessat
                if (fileExists) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean getPermissionInfo() {
        String[] lines = mountReader();

        if (lines == null) {
            return false;
        }

        int sdkVersion = android.os.Build.VERSION.SDK_INT;

        for (String line : lines) {
            String[] args = line.split(" ");
            // 检查每行信息是否包含足够的字段，旧版本的Android系统需要至少4个字段，新版本需要至少6个
            if ((sdkVersion <= android.os.Build.VERSION_CODES.M && args.length < 4)
                    || (sdkVersion > android.os.Build.VERSION_CODES.M && args.length < 6)) {
                continue;
            }

            String mountPoint;
            String mountOptions;

            // 根据Android版本确定挂载点和挂载选项的索引
            if (sdkVersion > android.os.Build.VERSION_CODES.M) {
                mountPoint = args[2];
                mountOptions = args[5];
            } else {
                mountPoint = args[1];
                mountOptions = args[3];
            }

            // 遍历所有不应具有读写权限的路径
            for (String pathToCheck : this.NotWritePermissionPaths) {
                if (mountPoint.equalsIgnoreCase(pathToCheck)) {
                    // 对于Android版本高于Marshmallow的设备，需要从挂载选项中移除括号
                    if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
                        mountOptions = mountOptions.replace("(", "");
                        mountOptions = mountOptions.replace(")", "");
                    }

                    // 分割挂载选项并检查是否包含"rw"，以确定该路径是否以读写模式挂载
                    for (String option : mountOptions.split(",")) {
                        if (option.equalsIgnoreCase("rw")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private String[] getPaths() {
        // 初始化路径列表, 获取 targetPaths 和 环境变量的 PATH
        ArrayList<String> paths = new ArrayList<>(Arrays.asList(targetPaths));
        String sysPaths = System.getenv("PATH");

        // 如果 PATH 为空, 直接返回 targetPaths
        if (sysPaths == null || "".equals(sysPaths)) {
            return paths.toArray(new String[0]);
        }

        // 如果 PATH 不为空, 遍历 PATH, 添加到路径列表中再返回
        for (String path : sysPaths.split(":")) {
            if (!path.endsWith("/")) {
                path = path + '/';
            }
            if (!paths.contains(path)) {
                paths.add(path);
            }
        }
        return paths.toArray(new String[0]);
    }

    private String[] mountReader() {
        try {
            InputStream inputstream = Runtime.getRuntime().exec("mount").getInputStream();
            if (inputstream == null) return null;

            // 使用正则表达式"\A"作为分隔符，一次性读取输入流的所有内容
            String propVal = new Scanner(inputstream).useDelimiter("\\A").next();
            return propVal.split("\n");
        } catch (IOException | NoSuchElementException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }
}
