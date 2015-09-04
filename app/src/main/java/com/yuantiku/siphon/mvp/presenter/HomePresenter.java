package com.yuantiku.siphon.mvp.presenter;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.mvp.imodel.IApkConfigModel;
import com.yuantiku.siphon.mvp.imodel.IFileEntryModel;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.imodel.IFileModelFactory;
import com.yuantiku.siphon.mvp.model.FileModelFactory;
import com.yuantiku.siphon.otto.BusFactory;
import com.yuantiku.siphon.otto.taskevent.SubmitTaskEvent;
import com.yuantiku.siphon.otto.taskevent.TaskFinishEvent;
import com.yuantiku.siphon.otto.taskevent.TaskProgressEvent;
import com.yuantiku.siphon.otto.taskevent.TaskStartEvent;
import com.yuantiku.siphon.task.DownloadApkTask;
import com.yuantiku.siphon.task.ITaskFactory;
import com.yuantiku.siphon.task.SyncTask;
import com.yuantiku.siphon.task.TaskFactory;

import java.util.List;

import bwzz.taskmanager.ITask;
import bwzz.taskmanager.TaskException;

/**
 * Created by wanghb on 15/9/3.
 */
public class HomePresenter extends BasePresenter {

    public interface IView {
        void renderApkConfig(ApkConfig apkConfig, FileEntry fileEntry, IFileModel apkFile);

        void renderSyncStart(ApkConfig apkConfig);

        void renderSyncFailed(ApkConfig apkConfig, TaskException e);

        void renderSyncSuccess(ApkConfig apkConfig, List<FileEntry> fileEntries);

        void renderDownloadStart(ApkConfig apkConfig);

        void renderDownloadProgress(ApkConfig apkConfig, FileEntry fileEntry, float percent);

        void renderDownloadSuccess(ApkConfig apkConfig, IFileModel result);

        void renderDownloadFailed(ApkConfig apkConfig, TaskException e);
    }

    public interface IHandler {
        void installApkFile(IFileModel apkFile);
    }

    private Bus bus = BusFactory.getBus();

    private boolean installAuto;

    private FileEntry fileEntry;

    private IFileModel apkFile;

    private ApkConfig apkConfig;

    private IView view = EmptyObjectFactory.createEmptyObject(IView.class);

    private IHandler handler = EmptyObjectFactory.createEmptyObject(IHandler.class);

    private ITaskFactory taskFactory = TaskFactory.getDefault();

    private IFileModelFactory fileModelFactory = FileModelFactory.getDefault();

    private IApkConfigModel apkConfigModel;

    private IFileEntryModel fileEntryModel;

    public HomePresenter(IPresenterManager presenterManager, IApkConfigModel apkConfigModel,
            IFileEntryModel fileEntryModel) {
        super(presenterManager);
        this.apkConfigModel = apkConfigModel;
        this.fileEntryModel = fileEntryModel;
        apkConfig = apkConfigModel.getDefault();
        fileEntry = fileEntryModel.getLatest(apkConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        view.renderApkConfig(apkConfig, fileEntry, apkFile);
        bus.register(this);
    }

    @Override
    public void onPause() {
        bus.unregister(this);
        super.onPause();
    }

    public HomePresenter attachView(IView view) {
        this.view = EmptyObjectFactory.ensureObject(view, IView.class);
        view.renderApkConfig(apkConfig, fileEntry, apkFile);
        return this;
    }

    public HomePresenter setHandler(IHandler handler) {
        this.handler = EmptyObjectFactory.ensureObject(handler, IHandler.class);
        return this;
    }

    public HomePresenter setTaskFactory(ITaskFactory taskFactory) {
        this.taskFactory = taskFactory;
        return this;
    }

    public HomePresenter setFileModelFactory(IFileModelFactory fileModelFactory) {
        this.fileModelFactory = fileModelFactory;
        return this;
    }

    public void updateApkConfig(ApkConfig apkConfig) {
        if (this.apkConfig.equals(apkConfig)) {
            return;
        }
        this.apkConfig = apkConfig;
        fileEntry = null;
        apkFile = null;
        view.renderApkConfig(apkConfig, fileEntry, apkFile);
        apkConfigModel.setDefault(apkConfig);
    }

    public void oneStep() {
        sync();
        installAuto = true;
    }

    public void sync() {
        installAuto = false;
        bus.post(new SubmitTaskEvent(taskFactory.createSyncTask(apkConfig.getListPath())));
        view.renderSyncStart(apkConfig);
    }

    public void downloadInstall() {
        if (apkFile != null && apkFile.exists()) {
            view.renderDownloadSuccess(apkConfig, apkFile);
            handler.installApkFile(apkFile);
        } else if (fileEntry != null) {
            bus.post(new SubmitTaskEvent(taskFactory.createDownloadTask(fileEntry)));
            view.renderDownloadStart(apkConfig);
        }
    }

    @Subscribe
    public void onTaskStartEvent(TaskStartEvent taskStartEvent) {
        ITask task = taskStartEvent.getTask();
        if (task instanceof SyncTask) {
            SyncTask syncTask = (SyncTask) task;
            view.renderSyncStart(apkConfig);
        } else if (task instanceof DownloadApkTask) {
            DownloadApkTask downloadApkTask = (DownloadApkTask) task;
            view.renderDownloadStart(apkConfig);
        }
    }

    @Subscribe
    public void onTaskProgressEvent(TaskProgressEvent taskProgressEvent) {
        view.renderDownloadProgress(apkConfig, fileEntry, taskProgressEvent.getPercent());
    }

    @Subscribe
    public void onTaskFinishEvent(TaskFinishEvent taskFinishEvent) {
        ITask task = taskFinishEvent.getTask();
        if (task.getTaskException() != null) {
            if (task instanceof SyncTask) {
                view.renderSyncFailed(apkConfig, task.getTaskException());
            } else {
                view.renderDownloadFailed(apkConfig, task.getTaskException());
            }
            return;
        }
        if (task instanceof SyncTask) {
            SyncTask syncTask = (SyncTask) task;
            final List<FileEntry> fileEntries = syncTask.getResult();
            view.renderSyncSuccess(apkConfig, fileEntries);
            if (fileEntries != null && !fileEntries.isEmpty()) {
                fileEntryModel.updateAll(fileEntries, apkConfig);
                fileEntry = fileEntries.get(0);
                IFileModel fileModel = fileModelFactory.createFileModel(fileEntry);
                if (fileModel.exists()) {
                    apkFile = fileModel;
                } else {
                    apkFile = null;
                }

                if (installAuto) {
                    downloadInstall();
                }
            }
        } else if (task instanceof DownloadApkTask) {
            DownloadApkTask downloadApkTask = (DownloadApkTask) task;
            apkFile = downloadApkTask.getResult();
            view.renderDownloadSuccess(apkConfig, apkFile);
            handler.installApkFile(apkFile);
        }
    }
}
