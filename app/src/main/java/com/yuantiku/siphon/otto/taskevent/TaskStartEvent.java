package com.yuantiku.siphon.otto.taskevent;

import bwzz.taskmanager.ITask;

/**
 * Created by wanghb on 15/8/20.
 */
public class TaskStartEvent extends BaseTaskEvent {
    public TaskStartEvent(ITask task) {
        super(task);
    }
}
