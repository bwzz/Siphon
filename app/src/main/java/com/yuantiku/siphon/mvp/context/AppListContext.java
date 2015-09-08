package com.yuantiku.siphon.mvp.context;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuantiku.siphon.constant.Key;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.helper.JsonHelper;
import com.yuantiku.siphon.mvp.presenter.AppListPresenter;
import com.yuantiku.siphon.mvp.presenter.PresenterFactory;
import com.yuantiku.siphon.mvp.viewmodel.AppListViewModel;

/**
 * Created by wanghb on 15/9/4.
 */
public class AppListContext extends BaseContext implements AppListViewModel.IHandler {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppListViewModel appListViewModel = new AppListViewModel(this);
        View view = appListViewModel.onCreateView(inflater, container, savedInstanceState);

        AppListPresenter appListPresenter = PresenterFactory.createAppListPresenter(this);
        appListPresenter.attachView(appListViewModel);
        return view;
    }

    @Override
    public void onAppSelected(ApkConfig apkConfig) {
        Intent intent = new Intent();
        intent.putExtra(Key.ApkConfig, JsonHelper.json(apkConfig));
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
