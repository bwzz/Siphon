package com.yuantiku.siphon.mvp.presenter;

import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.helper.CheckUpdateHelper;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.imodel.IFileModelFactory;
import com.yuantiku.siphon.mvp.model.FileModelFactory;
import com.yuantiku.siphon.task.DownloadTask;
import com.yuantiku.siphon.task.ITaskFactory;
import com.yuantiku.siphon.task.TaskFactory;

import bwzz.taskmanager.ITask;
import bwzz.taskmanager.ITaskReporter;
import im.fir.sdk.version.AppVersion;

/**
 * Created by wanghb on 15/9/4.
 */
public class CheckUpdatePresenter extends BasePresenter {
    public interface IView {
        void renderCheckStart();

        void renderCheckFinishWithNewVersion(AppVersion appVersion);

        void renderCheckFinishWithNoNewVersion(AppVersion oldVersion);

        void renderCheckFailed(AppVersion oldVersion, Exception e);

        void renderDownloadTaskStart(AppVersion appVersion);

        void renderDownloadTaskProgress(AppVersion appVersion, float percent);

        void renderDownloadTaskFinished(AppVersion appVersion, IFileModel apkFile);
    }

    public interface IHandler {
        void installApk(IFileModel apkFile);
    }

    private AppVersion appVersion;

    private IView view = EmptyObjectFactory.createEmptyObject(IView.class);

    private IHandler handler = EmptyObjectFactory.createEmptyObject(IHandler.class);

    private IFileModel apkFile;

    private ITaskFactory taskFactory = TaskFactory.getDefault();

    private IFileModelFactory fileModelFactory = FileModelFactory.getDefault();

    private String appName;
    private String cachePath;

    public CheckUpdatePresenter(IPresenterManager presenterManager, String appName, String cachePath) {
        super(presenterManager);
        this.appName = appName;
        this.cachePath = cachePath;
    }

    public void attachView(IView view) {
        this.view = EmptyObjectFactory.ensureObject(view, IView.class);
    }

    public void setHandler(IHandler handler) {
        this.handler = handler;
    }

    public void setAppVersion(AppVersion newVersion) {
        appVersion = newVersion;
        if (appVersion == null) {
            checkUpdate();
        } else {
            view.renderCheckFinishWithNewVersion(appVersion);
        }
    }

    public void downloadAndInstallApk() {
        if (apkFile != null && apkFile.exists()) {
            handler.installApk(apkFile);
            return;
        }

        String filename = String.format("%s-%s.apk", appName, appVersion.getVersionName());
        apkFile = fileModelFactory.createFileModel(String.format("%s/s", cachePath, filename));

        view.renderDownloadTaskStart(appVersion);

        DownloadTask task = taskFactory.createDownloadTask(appVersion.getUpdateUrl(),
                apkFile.getPath());
        task.run(new ITaskReporter() {
            @Override
            public void onTaskStart(ITask task) {
                view.renderDownloadTaskStart(appVersion);
            }

            @Override
            public void onTaskProgress(ITask task, float percent) {
                view.renderDownloadTaskProgress(appVersion, percent);
            }

            @Override
            public void onTaskCanceled(ITask task) {

            }

            @Override
            public void onTaskFinish(ITask task) {
                view.renderDownloadTaskFinished(appVersion, ((DownloadTask) task).getResult());
            }
        });
    }

    public void checkUpdate() {
        view.renderCheckStart();
        CheckUpdateHelper.checkUpdate(new CheckUpdateHelper.CheckUpdateCallback() {
            @Override
            public void onNewVersion(AppVersion appVersion) {
                view.renderCheckFinishWithNewVersion(appVersion);
                CheckUpdatePresenter.this.appVersion = appVersion;
            }

            @Override
            public void onNoNewVersion(AppVersion appVersion) {
                view.renderCheckFinishWithNoNewVersion(appVersion);
                CheckUpdatePresenter.this.appVersion = appVersion;
            }

            @Override
            public void onError(Exception e) {
                view.renderCheckFailed(CheckUpdatePresenter.this.appVersion, e);
            }

        });
    }
}
