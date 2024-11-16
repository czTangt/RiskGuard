package com.cztang.hook;

import com.cztang.riskguard.Activity.HookActivity;

public class Hook {
    protected HookActivity hookActivity;
    protected final static String TAG = "czTang";

    public Hook(HookActivity hookActivity) {
        this.hookActivity = hookActivity;
    }

    static {
        System.loadLibrary("hook");
    }

    public native String getHookInfo();
}
