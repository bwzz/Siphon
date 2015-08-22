package com.yuantiku.siphon.helper;

import android.content.Context;

import java.io.File;

/**
 * Created by wanghb on 15/8/22.
 */
public class PathHelper {
    public static File getCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    public static File getCacheFilePath(Context context, String fileName) {
        return new File(getCacheDir(context), fileName);
    }
}
