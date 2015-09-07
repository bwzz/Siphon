package com.yuantiku.siphon.dagger.component;

import com.yuantiku.siphon.dagger.module.ApplicationModule;
import com.yuantiku.siphon.mvp.context.FileEntriesContext;
import com.yuantiku.siphon.mvp.context.HomeContext;
import com.yuantiku.siphon.mvp.presenter.AppListPresenter;
import com.yuantiku.siphon.mvp.presenter.HomePresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by wanghb on 15/9/7.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(HomeContext homeContext);

    void inject(FileEntriesContext fileEntriesContext);

    void inject(AppListPresenter appListPresenter);

    void inject(HomePresenter homePresenter);

}
