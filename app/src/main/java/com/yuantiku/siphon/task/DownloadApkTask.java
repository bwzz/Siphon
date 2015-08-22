package com.yuantiku.siphon.task;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.webservice.ServiceFactory;

import java.io.File;

import bwzz.taskmanager.AbstractTask;
import bwzz.taskmanager.ITaskReporter;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanghb on 15/8/20.
 */
public class DownloadApkTask extends AbstractTask {
    private FileEntry fileEntry;

    private File targetFile;

    DownloadApkTask(FileEntry fileEntry) {
        super(fileEntry.href);
        this.fileEntry = fileEntry;
    }

    public File getTargetFile() {
        return targetFile;
    }

    @Override
    public void run(@Nullable ITaskReporter taskReporter) {
        String targetFilePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileEntry.name).getAbsolutePath();
        ServiceFactory.createDownloadService(targetFilePath)
                .get("android/102/alpha/" + fileEntry.name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    targetFile = new File(targetFilePath);
                    reportTaskFinish(taskReporter);
                }, error -> {
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
}
