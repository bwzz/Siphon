package com.yuantiku.siphon.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yuantiku.siphon.constant.Key;
import com.yuantiku.siphon.data.AppVersion;
import com.yuantiku.siphon.helper.AppHelper;
import com.yuantiku.siphon.helper.JsonHelper;
import com.yuantiku.siphon.helper.PathHelper;
import com.yuantiku.siphon.mvp.context.CheckUpdateContext;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.presenter.CheckUpdatePresenter;
import com.yuantiku.siphon.mvp.presenter.PresenterFactory;

import bwzz.activityCallback.ILauncher;
import bwzz.activityCallback.LaunchArgument;
import bwzz.activityCallback.LaunchHelper;
import bwzz.activityReuse.ContainerActivity;
import bwzz.activityReuse.FragmentPackage;
import bwzz.activityReuse.ReuseIntentBuilder;

/**
 * Created by wanghb on 15/8/22.
 */
public class CheckUpdateService extends Service implements ILauncher, CheckUpdatePresenter.IView {

    private CheckUpdatePresenter checkUpdatePresenter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkUpdatePresenter = PresenterFactory
                .createCheckUpdatePresenter(presenter -> {

        }, AppHelper.getAppName(this), PathHelper.getCacheDir(this).getPath());
        checkUpdatePresenter.attachView(this);
        checkUpdatePresenter.checkUpdate();
        return START_NOT_STICKY;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        startActivity(intent);
    }

    @Override
    public void renderCheckStart() {

    }

    @Override
    public void renderCheckFinishWithNewVersion(AppVersion appVersion) {
        checkUpdatePresenter.setHandler(apkFile -> openUpdate(appVersion));
        checkUpdatePresenter.downloadAndInstallApk();
    }

    @Override
    public void renderCheckFinishWithNoNewVersion(AppVersion oldVersion) {

    }

    @Override
    public void renderCheckFailed(AppVersion oldVersion, Throwable e) {

    }

    @Override
    public void renderDownloadTaskStart(AppVersion appVersion) {

    }

    @Override
    public void renderDownloadTaskProgress(AppVersion appVersion, float percent) {

    }

    @Override
    public void renderDownloadTaskFinished(AppVersion appVersion, IFileModel apkFile) {
        openUpdate(appVersion);
    }

    private void openUpdate(AppVersion appVersion) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.AppVersion, JsonHelper.json(appVersion));
        FragmentPackage fragmentPackage = new FragmentPackage();
        fragmentPackage.setContainer(android.R.id.content)
                .setArgument(bundle)
                .setFragmentClassName(CheckUpdateContext.class.getName());

        LaunchArgument argument = ReuseIntentBuilder.build()
                .activity(ContainerActivity.class)
                .fragmentPackage(fragmentPackage)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .getLaunchArgumentBuilder(CheckUpdateService.this)
                .get();

        LaunchHelper launchHelper = new LaunchHelper(CheckUpdateService.this);
        launchHelper.launch(argument);
    }

}
