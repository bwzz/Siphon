package com.yuantiku.siphon.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yuantiku.siphon.app.ApplicationComponentProvider;
import com.yuantiku.siphon.taskmanager.SyncTaskManager;

import javax.inject.Inject;

/**
 * Created by wanghb on 15/8/16.
 */
public class WorkService extends Service {

    @Inject
    SyncTaskManager syncTaskManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationComponentProvider.getApplicationComponent().inject(this);
        syncTaskManager.start();
    }

    @Override
    public void onDestroy() {
        syncTaskManager.stop();
        super.onDestroy();
    }
}
