package com.yuantiku.siphon.task;

import android.os.Looper;
import android.support.annotation.Nullable;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.webservice.ServiceFactory;

import java.util.List;

import bwzz.taskmanager.*;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanghb on 15/8/20.
 */
public class SyncTask extends AbstractTask<List<FileEntry>> {

    private ApkConfig apkConfig;

    SyncTask(ApkConfig apkConfig) {
        this(apkConfig, new TaskReportHandler(Looper.myLooper()));
    }

    SyncTask(ApkConfig apkConfig, TaskReportHandler handler) {
        super(apkConfig.getListPath(), handler);
        this.apkConfig = apkConfig;
    }

    public ApkConfig getApkConfig() {
        return apkConfig;
    }

    @Override
    public void run(@Nullable ITaskReporter taskReporter) {
        ServiceFactory.createZhenguanyuService()
                .listFiles(apkConfig.getListPath())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((list) -> {
                    setApkConfigForResult(list);
                    setResult(list);
                    reportTaskFinish(taskReporter);
                }, (error) -> {
                    setTaskException(TaskException.wrap(error));
                    reportTaskFinish(taskReporter);
                });
        taskReporter.onTaskStart(this);
    }

    private void reportTaskFinish(@Nullable ITaskReporter taskReporter) {
        if (isCanceled()) {
            taskReporter.onTaskCanceled(this);
        } else {
            taskReporter.onTaskFinish(this);
        }
    }

    private void setApkConfigForResult(List<FileEntry> fileEntries) {
        for (FileEntry fileEntry : fileEntries) {
            fileEntry.setApkConfig(apkConfig);
        }
    }
}
