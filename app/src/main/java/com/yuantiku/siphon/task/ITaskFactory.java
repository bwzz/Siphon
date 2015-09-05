package com.yuantiku.siphon.task;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;

/**
 * Created by wanghb on 15/9/3.
 */
public interface ITaskFactory {
    SyncTask createSyncTask(ApkConfig apkConfig);

    DownloadApkTask createDownloadTask(FileEntry fileEntry);

    DownloadTask createDownloadTask(String src, String target);
}
