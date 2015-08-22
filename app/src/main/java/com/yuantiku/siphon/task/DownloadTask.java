package com.yuantiku.siphon.task;

import android.support.annotation.Nullable;

import bwzz.taskmanager.AbstractTask;
import bwzz.taskmanager.ITaskReporter;

/**
 * Created by wanghb on 15/8/21.
 */
public class DownloadTask extends AbstractTask {
    private String target;
    private String source;

    public DownloadTask(@Nullable String source, @Nullable String target) {
        super(source);
        this.source = source;
        this.target = target;
    }

    @Override
    public void run(@Nullable ITaskReporter taskReporter) {

    }
}
