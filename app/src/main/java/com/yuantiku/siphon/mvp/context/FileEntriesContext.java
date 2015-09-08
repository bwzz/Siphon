package com.yuantiku.siphon.mvp.context;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuantiku.siphon.app.ApplicationComponentProvider;
import com.yuantiku.siphon.constant.Key;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.helper.ApkHelper;
import com.yuantiku.siphon.helper.JsonHelper;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.model.FileModelFactory;
import com.yuantiku.siphon.mvp.presenter.FileEntriesListPresenter;
import com.yuantiku.siphon.mvp.presenter.IPresenterManager;
import com.yuantiku.siphon.mvp.presenter.PresenterFactory;
import com.yuantiku.siphon.mvp.viewmodel.FileEntriesViewModel;

import javax.inject.Inject;

/**
 * Created by wanghb on 15/9/5.
 */
public class FileEntriesContext extends BaseContext implements FileEntriesViewModel.IHandler {
    private FileEntriesListPresenter fileEntriesListPresenter;
    @Inject
    FileModelFactory fileModelFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationComponentProvider.getApplicationComponent().inject(this);
    }

    @Override
    protected void createPresenters(@NonNull IPresenterManager presenterManager) {
        super.createPresenters(presenterManager);
        ApkConfig apkConfig = JsonHelper.json(getArguments().getString(Key.ApkConfig),
                ApkConfig.class);
        fileEntriesListPresenter = PresenterFactory.createFileEntriesListPresenter(
                presenterManager, apkConfig);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FileEntriesViewModel fileEntriesViewModel = new FileEntriesViewModel(fileModelFactory, this);
        View view = fileEntriesViewModel.onCreateView(inflater, container, savedInstanceState);
        fileEntriesListPresenter.attachView(fileEntriesViewModel);
        return view;
    }

    @Override
    public void clickFileEntry(FileEntry fileEntry) {
        IFileModel fileModel = fileModelFactory.createFileModel(fileEntry);
        if (fileModel.exists()) {
            ApkHelper.installApk(getActivity(), fileModel);
        } else {
            fileEntriesListPresenter.download(null, fileEntry);
        }
    }
}
