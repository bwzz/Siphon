package com.yuantiku.siphon.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

/**
 * Created by wanghb on 15/8/21.
 */
public class AppHelper {
    public static int getVersionCode(Context context) {
        PackageInfo info = getPackageInfo(context);
        return info.versionCode;
    }

    public static String getVersionName(Context context) {
        PackageInfo info = getPackageInfo(context);
        return info.versionName;
    }

    @NonNull
    private static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        PackageInfo info = new PackageInfo();
        return info;
    }

    public static String getAppName(Context context) {
        PackageInfo info = getPackageInfo(context);
        return info.applicationInfo.loadLabel(context.getPackageManager()).toString();
    }

}
