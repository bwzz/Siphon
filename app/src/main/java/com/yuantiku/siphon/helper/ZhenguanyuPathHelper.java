package com.yuantiku.siphon.helper;

import com.yuantiku.siphon.app.*;
import com.yuantiku.siphon.data.FileEntry;

import java.io.File;

/**
 * Created by wanghb on 15/8/22.
 */
public class ZhenguanyuPathHelper {
    private final static String EndPoint = "http://app.zhenguanyu.com";

    public static String getEndpoint() {
        return EndPoint;
    }

    public static String create(FileEntry fileEntry) {
        return getEndpoint() + fileEntry.href;
    }

    public static String createCachePath(FileEntry fileEntry) {
        String targetFilePath = new File(ApplicationFactory.getApplication().getExternalCacheDir(),
                fileEntry.name).getAbsolutePath();
        return targetFilePath;

    }
}
