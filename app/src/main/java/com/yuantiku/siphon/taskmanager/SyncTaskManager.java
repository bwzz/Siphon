package com.yuantiku.siphon.taskmanager;

import com.squareup.otto.Bus;
import com.yuantiku.siphon.otto.taskevent.TaskCanceledEvent;
import com.yuantiku.siphon.otto.taskevent.TaskFinishEvent;
import com.yuantiku.siphon.otto.taskevent.TaskProgressEvent;
import com.yuantiku.siphon.otto.taskevent.TaskStartEvent;

import bwzz.taskmanager.ITask;
import bwzz.taskmanager.ITaskReporter;
import bwzz.taskmanager.TaskManager;

/**
 * Created by wanghb on 15/8/20.
 */
public class SyncTaskManager extends TaskManager implements ITaskReporter {
    private Bus syncBus;

    SyncTaskManager(Bus syncBus) {
        super(10);
        this.syncBus = syncBus;
        setTaskReporter(this);
    }

    @Override
    public void start() {
        super.start();
        syncBus.register(this);
    }

    @Override
    public void stop() {
        syncBus.unregister(this);
        super.stop();
    }

    @Override
    public void onTaskStart(ITask task) {
        syncBus.post(new TaskStartEvent(task));
    }

    @Override
    public void onTaskProgress(ITask task, float percent) {
        syncBus.post(new TaskProgressEvent(task, percent));
    }

    @Override
    public void onTaskCanceled(ITask task) {
        syncBus.post(new TaskCanceledEvent(task));
    }

    @Override
    public void onTaskFinish(ITask task) {
        syncBus.post(new TaskFinishEvent(task));
    }
}
