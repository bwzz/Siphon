package com.yuantiku.siphon.task;

import bwzz.taskmanager.ITask;
import bwzz.taskmanager.TaskException;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.otto.taskevent.SubmitTaskEvent;
import com.yuantiku.siphon.otto.taskevent.TaskFinishEvent;
import com.yuantiku.siphon.otto.taskevent.TaskProgressEvent;
import com.yuantiku.siphon.otto.taskevent.TaskStartEvent;

import javax.inject.Inject;

/**
 * Created by wanghb on 15/9/5.
 */
public class DownloadHelper {

    public interface IHandler {
        void onDownloadStart(FileEntry fileEntry);

        void onDownloadSuccess(FileEntry fileEntry, IFileModel fileModel);

        void onDownloadProgress(FileEntry fileEntry, float percent);

        void onDownloadFailed(FileEntry fileEntry, TaskException e);
    }

    private final Bus bus;

    private final ITaskFactory taskFactory;

    private IHandler handler = EmptyObjectFactory.createEmptyObject(IHandler.class);

    @Inject
    DownloadHelper(Bus bus, ITaskFactory taskFactory) {
        this.bus = bus;
        this.taskFactory = taskFactory;
    }

    public void register() {
        bus.register(this);
    }

    public void unregister() {
        bus.unregister(this);
    }

    public void startDownload(FileEntry fileEntry, IHandler handler) {
        this.handler = EmptyObjectFactory.ensureObject(handler, IHandler.class);
        bus.post(new SubmitTaskEvent(taskFactory.createDownloadTask(fileEntry)));
    }

    @Subscribe
    public void onTaskStartEvent(TaskStartEvent taskStartEvent) {
        ITask task = taskStartEvent.getTask();
        if (!(task instanceof DownloadApkTask)) {
            return;
        }
        DownloadApkTask downloadApkTask = (DownloadApkTask) task;
        handler.onDownloadStart(downloadApkTask.getFileEntry());
    }

    @Subscribe
    public void onTaskProgressEvent(TaskProgressEvent taskProgressEvent) {
        ITask task = taskProgressEvent.getTask();
        if (!(task instanceof DownloadApkTask)) {
            return;
        }
        DownloadApkTask downloadApkTask = (DownloadApkTask) task;
        handler.onDownloadProgress(downloadApkTask.getFileEntry(),
                taskProgressEvent.getPercent());
    }

    @Subscribe
    public void onTaskFinishEvent(TaskFinishEvent taskFinishEvent) {
        ITask task = taskFinishEvent.getTask();
        if (!(task instanceof DownloadApkTask)) {
            return;
        }
        DownloadApkTask downloadApkTask = (DownloadApkTask) task;

        if (downloadApkTask.getTaskException() != null) {
            handler.onDownloadFailed(downloadApkTask.getFileEntry(),
                    downloadApkTask.getTaskException());
        } else {
            IFileModel apkFile = downloadApkTask.getResult();
            handler.onDownloadSuccess(downloadApkTask.getFileEntry(), apkFile);
        }
    }
}
