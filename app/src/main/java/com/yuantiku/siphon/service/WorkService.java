package com.yuantiku.siphon.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yuantiku.siphon.otto.BusFactory;
import com.yuantiku.siphon.otto.taskevent.SubmitTaskEvent;
import com.yuantiku.siphon.taskmanager.TaskManagerFactory;

import bwzz.log.LogCat;
import bwzz.taskmanager.TaskManager;

/**
 * Created by wanghb on 15/8/16.
 */
public class WorkService extends Service {
    private Bus bus = BusFactory.getBus();
    private LogCat L = LogCat.createInstance(this);

    private TaskManager syncTaskManager = TaskManagerFactory.getSyncTaskManager();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        syncTaskManager.start();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        syncTaskManager.stop();
        bus.unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onSubmitTaskEvent(SubmitTaskEvent submitTaskEvent) {
        syncTaskManager.addTask(submitTaskEvent.getTask());
    }
}
