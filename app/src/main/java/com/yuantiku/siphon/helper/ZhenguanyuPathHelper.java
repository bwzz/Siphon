package com.yuantiku.siphon.helper;

import android.os.Environment;

import com.yuantiku.siphon.data.FileEntry;

import java.io.File;

/**
 * Created by wanghb on 15/8/22.
 */
public class ZhenguanyuPathHelper {
    private final static String EndPoint = "https://app.zhenguanyu.com";

    public static String getEndpoint() {
        return EndPoint;
    }

    public static String create(FileEntry fileEntry) {
        return getEndpoint() + "/list?dir=android/102/alpha/" + fileEntry.name;
    }

    public static String createCachePath(FileEntry fileEntry) {
        String targetFilePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileEntry.name).getAbsolutePath();
        return targetFilePath;

    }
}
