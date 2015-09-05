package com.yuantiku.siphon.mvp.presenter;

import com.squareup.otto.Bus;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.mvp.imodel.IFileEntryModel;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.imodel.IFileModelFactory;
import com.yuantiku.siphon.mvp.model.FileModelFactory;
import com.yuantiku.siphon.otto.BusFactory;
import com.yuantiku.siphon.task.DownloadHelper;
import com.yuantiku.siphon.task.SyncHelper;

import java.util.List;

import bwzz.taskmanager.TaskException;

/**
 * Created by wanghb on 15/9/5.
 */
public class FileEntriesListPresenter extends BasePresenter {

    public interface IView {

        void renderSyncStart(ApkConfig apkConfig);

        void renderSyncFailed(ApkConfig apkConfig, TaskException e);

        void renderSyncSuccess(ApkConfig apkConfig, List<FileEntry> fileEntries);

        void renderDownloadStart(FileEntry fileEntry);

        void renderDownloadProgress(FileEntry fileEntry, float percent);

        void renderDownloadSuccess(FileEntry fileEntry, IFileModel result);

        void renderDownloadFailed(FileEntry fileEntry, TaskException e);
    }

    public interface IHandler {
        void installApkFile(IFileModel apkFile);
    }

    private Bus bus = BusFactory.getBus();

    private ApkConfig apkConfig;

    private IView view = EmptyObjectFactory.createEmptyObject(IView.class);

    private IHandler handler = EmptyObjectFactory.createEmptyObject(IHandler.class);

    private IFileModelFactory fileModelFactory = FileModelFactory.getDefault();

    private IFileEntryModel fileEntryModel;

    private SyncHelper syncHelper;

    private DownloadHelper downloadHelper;

    public FileEntriesListPresenter(IPresenterManager presenterManager,
            ApkConfig apkConfig,
            IFileEntryModel fileEntryModel) {
        super(presenterManager);
        this.apkConfig = apkConfig;
        this.fileEntryModel = fileEntryModel;
        syncHelper = new SyncHelper(bus);
        downloadHelper = new DownloadHelper(bus);
    }

    @Override
    public void onResume() {
        super.onResume();
        syncHelper.register();
        downloadHelper.register();
    }

    @Override
    public void onPause() {
        downloadHelper.unregister();
        syncHelper.unregister();
        super.onPause();
    }

    public FileEntriesListPresenter attachView(IView view) {
        this.view = EmptyObjectFactory.ensureObject(view, IView.class);
        List<FileEntry> fileEntries = fileEntryModel.list(apkConfig);
        view.renderSyncSuccess(apkConfig, fileEntries);
        return this;
    }

    public FileEntriesListPresenter setHandler(IHandler handler) {
        this.handler = EmptyObjectFactory.ensureObject(handler, IHandler.class);
        return this;
    }

    public FileEntriesListPresenter setFileModelFactory(IFileModelFactory fileModelFactory) {
        this.fileModelFactory = fileModelFactory;
        return this;
    }

    public void sync(SyncHelper.IHandler handler) {
        final SyncHelper.IHandler syncHander = EmptyObjectFactory.ensureObject(handler,
                SyncHelper.IHandler.class);
        syncHelper.startSync(apkConfig, new SyncHelper.IHandler() {
            @Override
            public void onSyncStart(ApkConfig apkConfig) {
                view.renderSyncStart(apkConfig);
                syncHander.onSyncStart(apkConfig);
            }

            @Override
            public void onSyncSuccess(ApkConfig apkConfig, List<FileEntry> fileEntries) {
                view.renderSyncSuccess(apkConfig, fileEntries);
                FileEntriesListPresenter.this.onSyncSuccess(apkConfig, fileEntries);
                syncHander.onSyncSuccess(apkConfig, fileEntries);
            }

            @Override
            public void onSyncFailed(ApkConfig apkConfig, TaskException e) {
                view.renderSyncFailed(apkConfig, e);
                syncHander.onSyncFailed(apkConfig, e);
            }
        });
    }

    public void download(DownloadHelper.IHandler iHandler, FileEntry fileEntry) {
        DownloadHelper.IHandler downloadHandler = EmptyObjectFactory.ensureObject(iHandler,
                DownloadHelper.IHandler.class);
        IFileModel apkFile = fileModelFactory.createFileModel(fileEntry);
        if (apkFile != null && apkFile.exists()) {
            view.renderDownloadSuccess(fileEntry, apkFile);
            downloadHandler.onDownloadSuccess(fileEntry, apkFile);
        } else if (fileEntry != null) {
            downloadHelper.startDownload(fileEntry, new DownloadHelper.IHandler() {
                @Override
                public void onDownloadStart(FileEntry fileEntry) {
                    view.renderDownloadStart(fileEntry);
                    downloadHandler.onDownloadStart(fileEntry);
                }

                @Override
                public void onDownloadSuccess(FileEntry fileEntry, IFileModel fileModel) {
                    view.renderDownloadSuccess(fileEntry, fileModel);
                    handler.installApkFile(fileModel);
                    downloadHandler.onDownloadSuccess(fileEntry, fileModel);
                }

                @Override
                public void onDownloadProgress(FileEntry fileEntry, float percent) {
                    view.renderDownloadProgress(fileEntry, percent);
                    downloadHandler.onDownloadProgress(fileEntry, percent);
                }

                @Override
                public void onDownloadFailed(FileEntry fileEntry, TaskException e) {
                    view.renderDownloadFailed(fileEntry, e);
                    downloadHandler.onDownloadFailed(fileEntry, e);
                }
            });
        }
    }

    private void onSyncSuccess(ApkConfig apkConfig, List<FileEntry> fileEntries) {
        fileEntryModel.updateAll(fileEntries, apkConfig);
    }
}
