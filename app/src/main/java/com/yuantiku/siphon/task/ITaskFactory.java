package com.yuantiku.siphon.task;

import com.yuantiku.siphon.data.FileEntry;

/**
 * Created by wanghb on 15/9/3.
 */
public interface ITaskFactory {
    SyncTask createSyncTask(String dir);

    DownloadApkTask createDownloadTask(FileEntry fileEntry);

    DownloadTask createDownloadTask(String src, String target);
}
