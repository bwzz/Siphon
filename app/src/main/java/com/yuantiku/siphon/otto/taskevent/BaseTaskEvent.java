package com.yuantiku.siphon.otto.taskevent;

import bwzz.taskmanager.ITask;

/**
 * Created by wanghb on 15/8/20.
 */
public class BaseTaskEvent {
    private ITask task;

    public BaseTaskEvent(ITask task) {
        this.task = task;
    }

    public ITask getTask() {
        return task;
    }
}
