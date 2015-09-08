package com.yuantiku.siphon.task;

import android.app.Application;
import android.content.Context;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.mvp.imodel.IFileModelFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by wanghb on 15/8/20.
 */
@Singleton
public class TaskFactory implements ITaskFactory {

    private final Context context;

    private final IFileModelFactory fileModelFactory;

    @Inject
    public TaskFactory(Application context, IFileModelFactory fileModelFactory) {
        this.context = context;
        this.fileModelFactory = fileModelFactory;
    }

    @Override
    public SyncTask createSyncTask(ApkConfig apkConfig) {
        return new SyncTask(apkConfig);
    }

    @Override
    public DownloadApkTask createDownloadTask(FileEntry fileEntry) {
        return new DownloadApkTask(fileModelFactory, context, fileEntry);
    }

    @Override
    public DownloadTask createDownloadTask(String src, String target) {
        return new DownloadTask(fileModelFactory, context, src, target);
    }
}
