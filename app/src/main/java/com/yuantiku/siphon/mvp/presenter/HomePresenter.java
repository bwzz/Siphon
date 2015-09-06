package com.yuantiku.siphon.mvp.presenter;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.mvp.imodel.IApkConfigModel;
import com.yuantiku.siphon.mvp.imodel.IFileEntryModel;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.imodel.IFileModelFactory;
import com.yuantiku.siphon.mvp.model.FileModelFactory;
import com.yuantiku.siphon.task.DownloadHelper;
import com.yuantiku.siphon.task.SyncHelper;

import java.util.List;

import bwzz.taskmanager.TaskException;

/**
 * Created by wanghb on 15/9/3.
 */
public class HomePresenter extends BasePresenter {

    public interface IView extends FileEntriesListPresenter.IView {
        void renderApkConfig(ApkConfig apkConfig, FileEntry fileEntry, IFileModel apkFile);
    }

    public interface IHandler {
        void installApkFile(IFileModel apkFile);
    }

    private boolean installAuto;

    private FileEntry fileEntry;

    private ApkConfig apkConfig;

    private IView view = EmptyObjectFactory.createEmptyObject(IView.class);

    private IHandler handler = EmptyObjectFactory.createEmptyObject(IHandler.class);

    private IFileModelFactory fileModelFactory = FileModelFactory.getDefault();

    private IApkConfigModel apkConfigModel;

    private IFileEntryModel fileEntryModel;

    private FileEntriesListPresenter fileEntriesListPresenter;

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
        view.renderApkConfig(apkConfig, fileEntry, getApkFile(fileEntry));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public HomePresenter attachView(IView view) {
        this.view = EmptyObjectFactory.ensureObject(view, IView.class);
        view.renderApkConfig(apkConfig, fileEntry, getApkFile(fileEntry));
        return this;
    }

    public HomePresenter setHandler(IHandler handler) {
        this.handler = EmptyObjectFactory.ensureObject(handler, IHandler.class);
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
        apkConfigModel.setDefault(apkConfig);

        fileEntry = fileEntryModel.getLatest(apkConfig);
        view.renderApkConfig(apkConfig, fileEntry, getApkFile(fileEntry));

        swapFileEntriesPresenter(view, fileEntryModel);
    }

    public void oneStep() {
        sync();
        installAuto = true;
    }

    public void sync() {
        installAuto = false;
        swapFileEntriesPresenter(view, fileEntryModel);
        fileEntriesListPresenter.sync(new SyncHelper.IHandler() {
            @Override
            public void onSyncStart(ApkConfig apkConfig) {

            }

            @Override
            public void onSyncSuccess(ApkConfig apkConfig, List<FileEntry> fileEntries) {
                HomePresenter.this.onSyncSuccess(apkConfig, fileEntries);
            }

            @Override
            public void onSyncFailed(ApkConfig apkConfig, TaskException e) {

            }
        });
    }

    public void downloadInstall() {
        IFileModel apkFile = getApkFile(fileEntry);
        if (apkFile != null && apkFile.exists()) {
            view.renderDownloadSuccess(fileEntry, apkFile);
            handler.installApkFile(apkFile);
        } else if (fileEntry != null) {
            fileEntriesListPresenter.download(new DownloadHelper.IHandler() {
                @Override
                public void onDownloadStart(FileEntry fileEntry) {

                }

                @Override
                public void onDownloadSuccess(FileEntry fileEntry, IFileModel fileModel) {
                    handler.installApkFile(fileModel);
                }

                @Override
                public void onDownloadProgress(FileEntry fileEntry, float percent) {

                }

                @Override
                public void onDownloadFailed(FileEntry fileEntry, TaskException e) {

                }
            }, fileEntry);
        }
    }

    private void onSyncSuccess(ApkConfig apkConfig, List<FileEntry> fileEntries) {
        fileEntryModel.updateAll(fileEntries, apkConfig);
        if (!fileEntries.isEmpty()) {
            fileEntry = fileEntries.get(0);
            if (installAuto) {
                downloadInstall();
            }
        }
    }

    private IFileModel getApkFile(FileEntry fileEntry) {
        if (fileEntry == null) {
            return null;
        }
        return fileModelFactory.createFileModel(fileEntry);
    }

    private void swapFileEntriesPresenter(IView view, IFileEntryModel fileEntryModel) {
        if (fileEntriesListPresenter != null) {
            fileEntriesListPresenter.onPause();
        }
        fileEntriesListPresenter = new FileEntriesListPresenter(presenter -> {}, apkConfig,
                fileEntryModel);
        fileEntriesListPresenter.onResume();
        fileEntriesListPresenter.attachView(view);
    }

}
