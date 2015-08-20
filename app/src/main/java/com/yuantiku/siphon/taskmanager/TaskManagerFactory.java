package com.yuantiku.siphon.taskmanager;

import com.yuantiku.siphon.otto.BusFactory;

import bwzz.taskmanager.TaskManager;

/**
 * Created by wanghb on 15/8/20.
 */
public class TaskManagerFactory {
    private static TaskManager syncTaskManager;

    public static TaskManager getSyncTaskManager() {
        if (syncTaskManager == null) {
            syncTaskManager = new SyncTaskManager(BusFactory.getBus());
        }
        return syncTaskManager;
    }
}
