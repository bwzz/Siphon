package com.yuantiku.siphon.otto.taskevent;

import bwzz.taskmanager.ITask;

/**
 * Created by wanghb on 15/8/20.
 */
public class TaskCanceledEvent extends BaseTaskEvent {
    public TaskCanceledEvent(ITask task) {
        super(task);
    }
}
