package com.yuantiku.siphon.task;

import bwzz.taskmanager.ITask;
import bwzz.taskmanager.TaskException;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.otto.taskevent.SubmitTaskEvent;
import com.yuantiku.siphon.otto.taskevent.TaskFinishEvent;
import com.yuantiku.siphon.otto.taskevent.TaskStartEvent;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wanghb on 15/9/5.
 */
public class SyncHelper {

    public interface IHandler {
        void onSyncStart(ApkConfig apkConfig);

        void onSyncSuccess(ApkConfig apkConfig, List<FileEntry> fileEntries);

        void onSyncFailed(ApkConfig apkConfig, TaskException e);
    }

    private final Bus bus;

    private final ITaskFactory taskFactory;

    private IHandler handler = EmptyObjectFactory.createEmptyObject(IHandler.class);

    @Inject
    SyncHelper(Bus bus, ITaskFactory taskFactory) {
        this.bus = bus;
        this.taskFactory = taskFactory;
    }

    public void register() {
        bus.register(this);
    }

    public void unregister() {
        bus.unregister(this);
    }

    public void startSync(ApkConfig apkConfig, IHandler iHandler) {
        this.handler = iHandler;
        bus.post(new SubmitTaskEvent(taskFactory.createSyncTask(apkConfig)));
    }

    @Subscribe
    public void onTaskStartEvent(TaskStartEvent taskStartEvent) {
        ITask task = taskStartEvent.getTask();
        if (task instanceof SyncTask) {
            SyncTask syncTask = (SyncTask) task;
            handler.onSyncStart(syncTask.getApkConfig());
        }
    }

    @Subscribe
    public void onTaskFinishEvent(TaskFinishEvent taskFinishEvent) {
        ITask task = taskFinishEvent.getTask();
        if (!(task instanceof SyncTask)) {
            return;
        }
        SyncTask syncTask = (SyncTask) task;
        if (syncTask.getTaskException() != null) {
            handler.onSyncFailed(syncTask.getApkConfig(), task.getTaskException());
        } else {
            final List<FileEntry> fileEntries = syncTask.getResult();
            handler.onSyncSuccess(syncTask.getApkConfig(), fileEntries);
        }
    }
}
