package com.cztang.device;


import com.cztang.riskguard.Activity.DeviceActivity;

public class Device {
    protected DeviceActivity deviceActivity;
    protected final static String TAG = "czTang";

    public Device(DeviceActivity deviceActivity) {
        this.deviceActivity = deviceActivity;
    }
}
