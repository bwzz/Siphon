package com.yuantiku.siphon.taskmanager;

import bwzz.taskmanager.ITask;
import bwzz.taskmanager.ITaskReporter;
import bwzz.taskmanager.TaskManager;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yuantiku.siphon.otto.taskevent.SubmitTaskEvent;
import com.yuantiku.siphon.otto.taskevent.TaskCanceledEvent;
import com.yuantiku.siphon.otto.taskevent.TaskFinishEvent;
import com.yuantiku.siphon.otto.taskevent.TaskProgressEvent;
import com.yuantiku.siphon.otto.taskevent.TaskStartEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by wanghb on 15/8/20.
 */
@Singleton
public class SyncTaskManager extends TaskManager implements ITaskReporter {
    private final Bus syncBus;

    @Inject
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

    @Subscribe
    public void onSubmitTaskEvent(SubmitTaskEvent submitTaskEvent) {
        addTask(submitTaskEvent.getTask());
    }
}
