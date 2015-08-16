package com.yuantiku.siphon.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.otto.BusFactory;
import com.yuantiku.siphon.otto.DownloadTaskEvent;
import com.yuantiku.siphon.otto.TaskEvent;
import com.yuantiku.siphon.otto.TaskResultEvent;
import com.yuantiku.siphon.webservice.ServiceFactory;

import java.io.File;
import java.util.LinkedList;

import bwzz.log.LogCat;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanghb on 15/8/16.
 */
public class WorkService extends Service {
    private Bus bus = BusFactory.createBus();
    private LogCat L = LogCat.createInstance(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    private TaskResultEvent latestTaskResultEvent;

    @Subscribe
    public void onTaskEvent(TaskEvent taskEvent) {
        ServiceFactory.createZhenguanyuService()
                .listFiles("android/102/alpha")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((list) -> {
                    TaskResultEvent<FileEntry> taskResultEvent = new TaskResultEvent();
                    taskResultEvent.fileEntries = list;
                    bus.post(taskResultEvent);
                    latestTaskResultEvent = taskResultEvent;
                }, (error) -> {
                    L.e(error);
                });
    }

    @Produce
    public TaskResultEvent<FileEntry> produceTaskEvent() {
        if (latestTaskResultEvent != null) {
            return latestTaskResultEvent;
        }
        TaskResultEvent<FileEntry> taskResultEvent = new TaskResultEvent();
        taskResultEvent.fileEntries = new LinkedList<>();
        FileEntry fileEntry = new FileEntry();
        fileEntry.date = "mock data";
        fileEntry.name = "fake name";
        taskResultEvent.fileEntries.add(fileEntry);
        return taskResultEvent;
    }

    @Subscribe
    public void onDownloadTaskEvent(DownloadTaskEvent downloadTaskEvent) {
        FileEntry fileEntry = downloadTaskEvent.fileEntry;
        String targetFilePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileEntry.name).getAbsolutePath();
        ServiceFactory.createDownloadService(targetFilePath)
                .get("android/102/alpha/" + fileEntry.name)
                .subscribe(file -> {
                    L.i("download success to : ", file.getAbsolutePath());
                }, error -> {
                    L.e(error);
                });
    }
}
