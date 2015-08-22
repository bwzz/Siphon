package com.yuantiku.siphon.task;

import android.support.annotation.Nullable;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;
import com.yuantiku.siphon.app.ApplicationFactory;

import java.io.File;

import bwzz.taskmanager.AbstractTask;
import bwzz.taskmanager.ITaskReporter;
import bwzz.taskmanager.TaskException;

/**
 * Created by wanghb on 15/8/21.
 */
public class DownloadTask extends AbstractTask<File> {
    private String target;
    private String source;
    private Future<File> future;

    public DownloadTask(@Nullable String source, @Nullable String target) {
        super(source);
        this.source = source;
        this.target = target;
    }

    @Override
    public void cancel() {
        super.cancel();
        if (future != null) {
            future.cancel();
        }
    }

    @Override
    public void run(@Nullable ITaskReporter taskReporter) {
        future = Ion
                .with(ApplicationFactory.getApplication())
                .load(source)
                .progress((downloaded, total) -> taskReporter.onTaskProgress(this, (float) (downloaded * 100d / total)))
                .write(new File(target))
                .setCallback((e, result) -> {
                    setTaskException(TaskException.wrap(e));
                    setResult(result);
                    taskReporter.onTaskFinish(this);
                });
        taskReporter.onTaskStart(this);
    }

}
