package com.cztang.device;


import com.cztang.riskguard.Activity.DeviceActivity;

public class Device {
    protected DeviceActivity deviceActivity;
    protected final static String TAG = "czTang";

    public Device(DeviceActivity deviceActivity) {
        this.deviceActivity = deviceActivity;
    }

    static {
        System.loadLibrary("device");
    }

    public native String getHardDiskInfor();

    public native String getKernelInfor();
}
