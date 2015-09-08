package com.yuantiku.siphon.mvp.presenter;

import android.support.annotation.NonNull;

import com.yuantiku.siphon.dagger.component.ApplicationComponent;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;

/**
 * @author wanghb
 * @date 15/9/8.
 */
public class PresenterFactory {

    private static ApplicationComponent applicationComponent;

    public static void initApplicationComponent(@NonNull ApplicationComponent
            applicationComponent) {
        PresenterFactory.applicationComponent = applicationComponent;
    }

    public static AppListPresenter createAppListPresenter(IPresenterManager presenterManager) {
        AppListPresenter appListPresenter = new AppListPresenter(presenterManager);
        applicationComponent.inject(appListPresenter);
        return appListPresenter;
    }

    public static CheckUpdatePresenter createCheckUpdatePresenter(
            IPresenterManager presenterManager, String appName, String cachePath) {
        CheckUpdatePresenter checkUpdatePresenter = new CheckUpdatePresenter(presenterManager,
                appName, cachePath);
        applicationComponent.inject(checkUpdatePresenter);
        return checkUpdatePresenter;
    }

    public static FileEntriesListPresenter createFileEntriesListPresenter(
            IPresenterManager presenterManager, ApkConfig apkConfig) {
        FileEntriesListPresenter fileEntriesListPresenter = new FileEntriesListPresenter
                (presenterManager, apkConfig);
        applicationComponent.inject(fileEntriesListPresenter);
        return fileEntriesListPresenter;
    }

    public static HomePresenter createHomePresenter(IPresenterManager presenterManager) {
        HomePresenter homePresenter = new HomePresenter(presenterManager);
        applicationComponent.inject(homePresenter);
        return homePresenter;
    }
}
