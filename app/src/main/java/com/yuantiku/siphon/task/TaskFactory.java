package com.yuantiku.siphon.task;

import com.yuantiku.siphon.data.FileEntry;

/**
 * Created by wanghb on 15/8/20.
 */
public class TaskFactory {
    public static SyncTask createSyncTask(String dir) {
        return new SyncTask(dir);
    }

    public static DownloadApkTask createDownloadTask(FileEntry fileEntry) {
        return new DownloadApkTask(fileEntry);
    }

    public static DownloadTask createDownloadTask(String src, String target) {
        return new DownloadTask(src, target);
    }
}
