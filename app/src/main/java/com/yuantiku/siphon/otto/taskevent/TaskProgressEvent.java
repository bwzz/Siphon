package com.yuantiku.siphon.otto.taskevent;

import bwzz.taskmanager.ITask;

/**
 * Created by wanghb on 15/8/20.
 */
public class TaskProgressEvent extends BaseTaskEvent {
    private float percent;

    public TaskProgressEvent(ITask task, float percent) {
        super(task);
        this.percent = percent;
    }
}
