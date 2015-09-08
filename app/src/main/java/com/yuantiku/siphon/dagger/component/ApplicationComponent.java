package com.yuantiku.siphon.dagger.component;

import com.yuantiku.siphon.dagger.module.ApplicationModule;
import com.yuantiku.siphon.mvp.context.FileEntriesContext;
import com.yuantiku.siphon.mvp.presenter.AppListPresenter;
import com.yuantiku.siphon.mvp.presenter.CheckUpdatePresenter;
import com.yuantiku.siphon.mvp.presenter.FileEntriesListPresenter;
import com.yuantiku.siphon.mvp.presenter.HomePresenter;
import com.yuantiku.siphon.service.WorkService;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by wanghb on 15/9/7.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(WorkService workService);

    // region Inject presenters
    void inject(AppListPresenter appListPresenter);

    void inject(HomePresenter homePresenter);

    void inject(FileEntriesListPresenter fileEntriesListPresenter);

    void inject(CheckUpdatePresenter checkUpdatePresenter);

    // endregion
    void inject(FileEntriesContext fileEntriesContext);

}
