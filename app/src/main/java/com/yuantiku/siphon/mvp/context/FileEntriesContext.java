package com.yuantiku.siphon.mvp.context;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.helper.ApkHelper;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.model.ApkConfigModel;
import com.yuantiku.siphon.mvp.model.FileEntryModel;
import com.yuantiku.siphon.mvp.model.FileModelFactory;
import com.yuantiku.siphon.mvp.presenter.FileEntriesListPresenter;
import com.yuantiku.siphon.mvp.presenter.IPresenterManager;
import com.yuantiku.siphon.mvp.viewmodel.FileEntriesViewModel;

/**
 * Created by wanghb on 15/9/5.
 */
public class FileEntriesContext extends BaseContext implements FileEntriesViewModel.IHandler {
    private FileEntriesListPresenter fileEntriesListPresenter;

    @Override
    protected void createPresenters(@NonNull IPresenterManager presenterManager) {
        super.createPresenters(presenterManager);
        fileEntriesListPresenter = new FileEntriesListPresenter(presenterManager,
                ApkConfigModel.getDefaultApkConfigModel().getDefault(),
                new FileEntryModel(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FileEntriesViewModel fileEntriesViewModel = new FileEntriesViewModel(this);
        View view = fileEntriesViewModel.onCreateView(inflater, container, savedInstanceState);
        fileEntriesListPresenter.attachView(fileEntriesViewModel);
        return view;
    }

    @Override
    public void clickFileEntry(FileEntry fileEntry) {
        IFileModel fileModel = FileModelFactory.getDefault().createFileModel(fileEntry);
        if (fileModel.exists()) {
            ApkHelper.installApk(getActivity(), fileModel);
        } else {
            fileEntriesListPresenter.download(null, fileEntry);
        }
    }
}
