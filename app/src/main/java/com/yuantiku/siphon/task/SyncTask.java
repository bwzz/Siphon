package com.yuantiku.siphon.task;

import android.os.Looper;
import android.support.annotation.Nullable;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.webservice.ServiceFactory;

import java.util.List;

import bwzz.taskmanager.*;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanghb on 15/8/20.
 */
public class SyncTask extends AbstractTask<List<FileEntry>> {

    private String dir;

    SyncTask(String dir) {
        this(dir, new TaskReportHandler(Looper.myLooper()));
    }

    SyncTask(String dir, TaskReportHandler handler) {
        super(dir, handler);
        this.dir = dir;
    }

    @Override
    public void run(@Nullable ITaskReporter taskReporter) {
        ServiceFactory.createZhenguanyuService()
                .listFiles(dir)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((list) -> {
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

}