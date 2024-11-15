package com.cztang.device;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.cztang.riskguard.Activity.DeviceActivity;


public class SerialInfo extends Device {
    public SerialInfo(DeviceActivity deviceActivity) {
        super(deviceActivity);
    }

    private String getSerialNumbers1() {
        @SuppressLint("HardwareIds")
        String serial = Settings.Secure.getString(deviceActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
        return serial;
    }

    private String getSerialNumbers2() {
        try {
            Bundle callResult = deviceActivity.getContentResolver().call(
                    Uri.parse("content://settings/secure"), "GET_secure", "android_id", new Bundle()
            );
            assert callResult != null;
            return callResult.getString("value");
        } catch (Exception e) {
            Log.e("czTang", e.toString());
        }
        return null;
    }


    public String getAndroidId() {
        String serial1 = getSerialNumbers1();
        String serial2 = getSerialNumbers2();

        if (serial1.equals(serial2)) {
            Log.i(TAG, "Serial Number: " + serial1);
            return "Serial Number: " + serial1;
        } else {
            return "Error: Failed to get serial number ";
        }
    }
}
