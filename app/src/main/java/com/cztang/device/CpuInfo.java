package com.cztang.device;


import com.cztang.riskguard.Activity.DeviceActivity;

import java.io.IOException;
import java.io.InputStream;

public class CpuInfo extends Device {
    public CpuInfo(DeviceActivity deviceActivity) {
        super(deviceActivity);
    }

    public String getCurCpuFreq() {
        StringBuilder result = new StringBuilder();
        String[] args = {"cat", "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"};
        ProcessBuilder cmd = new ProcessBuilder(args);

        try {
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] buf = new byte[16];
            while (in.read(buf) != -1) {
                result.append(new String(buf));
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Cpu Cur Freq: " + result.toString().trim();
    }
}
