package com.yuantiku.siphon.task;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;

/**
 * Created by wanghb on 15/8/20.
 */
public class TaskFactory implements ITaskFactory {

    public static TaskFactory getDefault() {
        return new TaskFactory();
    }

    @Override
    public SyncTask createSyncTask(ApkConfig apkConfig) {
        return new SyncTask(apkConfig);
    }

    @Override
    public DownloadApkTask createDownloadTask(FileEntry fileEntry) {
        return new DownloadApkTask(fileEntry);
    }

    @Override
    public DownloadTask createDownloadTask(String src, String target) {
        return new DownloadTask(src, target);
    }
}
