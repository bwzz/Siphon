package com.yuantiku.siphon.app;

import android.app.Application;

import im.fir.sdk.FIR;

/**
 * Created by wanghb on 15/8/21.
 */
public class Siphon extends Application {
    @Override
    public void onCreate() {
        FIR.init(this);
        super.onCreate();
        FIR.setDebug(false);
    }
}
