package com.yuantiku.siphon.task;

import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;
import com.yuantiku.siphon.app.ApplicationFactory;
import com.yuantiku.siphon.mvp.model.FileModelFactory;
import com.yuantiku.siphon.mvp.imodel.IFileModel;

import java.io.File;

import bwzz.taskmanager.AbstractTask;
import bwzz.taskmanager.ITaskReporter;
import bwzz.taskmanager.TaskException;
import bwzz.taskmanager.TaskReportHandler;

/**
 * Created by wanghb on 15/8/21.
 */
public class DownloadTask extends AbstractTask<IFileModel> {
    private String target;
    private String source;
    private Future<File> future;

    public DownloadTask(@Nullable String source, @Nullable String target) {
        this(source, target, new TaskReportHandler(Looper.getMainLooper()));
    }

    public DownloadTask(@Nullable String source, @Nullable String target, TaskReportHandler handler) {
        super(source, handler);
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
                .setLogging("DownloadTask", Log.DEBUG)
                .progress(
                        (downloaded, total) -> taskReporter.onTaskProgress(this,
                                (float) (downloaded * 100d / total)))
                .write(new File(target))
                .setCallback(
                        (e, result) -> {
                            if (e != null) {
                                setTaskException(TaskException.wrap(e));
                            }
                            IFileModel fileModel = new FileModelFactory().createFileModel(result
                                    .getAbsolutePath());
                            setResult(fileModel);
                            taskReporter.onTaskFinish(this);
                        });
        taskReporter.onTaskStart(this);
    }

}
