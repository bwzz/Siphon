package com.yuantiku.siphon.app;

import android.app.Application;

/**
 * Created by wanghb on 15/8/22.
 */
public class ApplicationFactory {
    public static Application getApplication() {
        return Siphon.application;
    }
}
