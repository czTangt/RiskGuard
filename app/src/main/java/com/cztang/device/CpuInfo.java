package com.cztang.device;


import android.os.Build;

import com.cztang.riskguard.Activity.DeviceActivity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CpuInfo extends Device {
    public CpuInfo(DeviceActivity deviceActivity) {
        super(deviceActivity);
    }

    private String readSystemFile(String filePath) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private String getCpuModel() {
        return Build.HARDWARE;
    }

    private int getCpuCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    private String getCpuMaxFreq() {
        String maxFreq = readSystemFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
        return maxFreq;
    }

    private String getCpuMinFreq() {
        String minFreq = readSystemFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq");
        return minFreq;
    }

    public String getCpuInfo() {
        return "Cpu Model: " + getCpuModel() + "\n" +
                "Cpu Cores: " + getCpuCores() + "\n" +
                "Cpu Max Freq: " + getCpuMaxFreq() + "\n" +
                "Cpu Min Freq: " + getCpuMinFreq();
    }

}
