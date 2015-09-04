package com.yuantiku.siphon.task;

import com.yuantiku.siphon.data.FileEntry;

/**
 * Created by wanghb on 15/8/20.
 */
public class TaskFactory implements ITaskFactory {

    public static TaskFactory getDefault() {
        return new TaskFactory();
    }

    @Override
    public SyncTask createSyncTask(String dir) {
        return new SyncTask(dir);
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
