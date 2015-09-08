package com.yuantiku.siphon.mvp.context;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuantiku.siphon.constant.Key;
import com.yuantiku.siphon.helper.ApkHelper;
import com.yuantiku.siphon.helper.AppHelper;
import com.yuantiku.siphon.helper.JsonHelper;
import com.yuantiku.siphon.helper.PathHelper;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.presenter.CheckUpdatePresenter;
import com.yuantiku.siphon.mvp.presenter.PresenterFactory;
import com.yuantiku.siphon.mvp.viewmodel.CheckUpdateViewModel;

import im.fir.sdk.version.AppVersion;

/**
 * Created by wanghb on 15/9/4.
 */
public class CheckUpdateContext extends BaseContext implements CheckUpdateViewModel.IHandler,
        CheckUpdatePresenter.IHandler {
    private CheckUpdatePresenter checkUpdatePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CheckUpdateViewModel checkUpdateViewModel = new CheckUpdateViewModel(this);
        View view = checkUpdateViewModel.onCreateView(inflater, container, savedInstanceState);

        checkUpdatePresenter = PresenterFactory.createCheckUpdatePresenter(this,
                AppHelper.getAppName(getActivity()),
                PathHelper.getCacheDir(getActivity()).getPath());
        checkUpdatePresenter.attachView(checkUpdateViewModel);

        AppVersion appVersion = null;
        if (getArguments() != null) {
            String appVersionStr = getArguments().getString(Key.AppVersion);
            appVersion = JsonHelper.json(appVersionStr, AppVersion.class);
        }
        checkUpdatePresenter.setAppVersion(appVersion);
        checkUpdatePresenter.setHandler(this);
        return view;
    }

    @Override
    public void downloadApk() {
        checkUpdatePresenter.downloadAndInstallApk();
    }

    @Override
    public void checkUpdate() {
        checkUpdatePresenter.checkUpdate();
    }

    @Override
    public void installApk(IFileModel apkFile) {
        ApkHelper.installApk(getActivity(), apkFile);
    }
}
