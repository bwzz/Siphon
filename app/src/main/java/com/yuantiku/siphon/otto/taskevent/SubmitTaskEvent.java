package com.yuantiku.siphon.otto.taskevent;

import bwzz.taskmanager.ITask;

/**
 * Created by wanghb on 15/8/20.
 */
public class SubmitTaskEvent extends BaseTaskEvent {
    public SubmitTaskEvent(ITask task) {
        super(task);
    }
}
