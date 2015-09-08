package com.yuantiku.siphon.task;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import bwzz.taskmanager.AbstractTask;
import bwzz.taskmanager.ITaskReporter;
import bwzz.taskmanager.TaskException;
import bwzz.taskmanager.TaskReportHandler;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.imodel.IFileModelFactory;

import java.io.File;

/**
 * Created by wanghb on 15/8/21.
 */
public class DownloadTask extends AbstractTask<IFileModel> {
    private final Context context;
    private final String target;
    private final String source;
    private Future<File> future;
    private final IFileModelFactory fileModelFactory;

    DownloadTask(@NonNull IFileModelFactory fileModelFactory, @NonNull Context context,
            @NonNull String source, @NonNull String target) {
        this(fileModelFactory, context, source, target, new TaskReportHandler(
                Looper.getMainLooper()));
    }

    DownloadTask(@NonNull IFileModelFactory fileModelFactory, @NonNull Context context,
            @NonNull String source, @NonNull String target, TaskReportHandler handler) {
        super(source, handler);
        this.context = context;
        this.source = source;
        this.target = target;
        this.fileModelFactory = fileModelFactory;
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
                .with(context)
                .load(source)
                .progress(
                        (downloaded, total) -> taskReporter.onTaskProgress(this,
                                (float) (downloaded * 100d / total)))
                .write(new File(target))
                .setCallback(
                        (e, result) -> {
                            if (e != null) {
                                setTaskException(TaskException.wrap(e));
                            }
                            IFileModel fileModel = fileModelFactory.createFileModel(result
                                    .getAbsolutePath());
                            setResult(fileModel);
                            taskReporter.onTaskFinish(this);
                        });
        taskReporter.onTaskStart(this);
    }

}
