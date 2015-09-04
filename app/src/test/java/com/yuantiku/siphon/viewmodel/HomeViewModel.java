package com.yuantiku.siphon.viewmodel;

import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.presenter.HomePresenter;

import java.util.List;

import bwzz.taskmanager.TaskException;

public class HomeViewModel implements HomePresenter.IView {

    private HomePresenter.IView delegate = EmptyObjectFactory
            .createEmptyObject(HomePresenter.IView.class);

    public void setDelegate(HomePresenter.IView delegate) {
        this.delegate = delegate;
    }

    public void renderApkConfig(ApkConfig apkConfig, FileEntry fileEntry, IFileModel apkFile) {
        delegate.renderApkConfig(apkConfig, fileEntry, apkFile);
    }

    public void renderSyncStart(ApkConfig apkConfig) {
        delegate.renderSyncStart(apkConfig);
    }

    public void renderSyncFailed(ApkConfig apkConfig, TaskException e) {
        delegate.renderSyncFailed(apkConfig, e);
    }

    public void renderSyncSuccess(ApkConfig apkConfig, List<FileEntry> fileEntries) {
        delegate.renderSyncSuccess(apkConfig, fileEntries);
    }

    public void renderDownloadStart(ApkConfig apkConfig) {
        delegate.renderDownloadStart(apkConfig);
    }

    public void renderDownloadProgress(ApkConfig apkConfig, FileEntry fileEntry, float percent) {
        delegate.renderDownloadProgress(apkConfig, fileEntry, percent);
    }

    public void renderDownloadSuccess(ApkConfig apkConfig, IFileModel result) {
        delegate.renderDownloadSuccess(apkConfig, result);
    }

    public void renderDownloadFailed(ApkConfig apkConfig, TaskException e) {
        delegate.renderDownloadFailed(apkConfig, e);
    }
}