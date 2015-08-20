package com.yuantiku.siphon.task;

import com.yuantiku.siphon.data.FileEntry;

import bwzz.taskmanager.ITask;

/**
 * Created by wanghb on 15/8/20.
 */
public class TaskFactory {
    public static SyncTask createSyncTask(String dir) {
        return new SyncTask(dir);
    }

    public static DownloadTask createDownloadTask(FileEntry fileEntry) {
        return new DownloadTask(fileEntry);
    }
}
