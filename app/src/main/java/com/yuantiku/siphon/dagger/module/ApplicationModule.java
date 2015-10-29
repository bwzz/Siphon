package com.yuantiku.siphon.dagger.module;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.yuantiku.siphon.app.Siphon;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.mvp.imodel.IApkConfigModel;
import com.yuantiku.siphon.mvp.imodel.IFileEntryModel;
import com.yuantiku.siphon.mvp.imodel.IFileModelFactory;
import com.yuantiku.siphon.mvp.model.ApkConfigModel;
import com.yuantiku.siphon.mvp.model.FileEntryModel;
import com.yuantiku.siphon.mvp.model.FileModelFactory;
import com.yuantiku.siphon.task.ITaskFactory;
import com.yuantiku.siphon.task.TaskFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wanghb on 15/9/7.
 */
@Singleton
@Module
public class ApplicationModule {

    private final Siphon siphon;

    public ApplicationModule(Siphon siphon) {
        this.siphon = siphon;
    }

    @Provides
    @Singleton
    Application provideApplicationContext() {
        return this.siphon;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return this.siphon;
    }

    @Provides
    ApkConfig provideDefaultApkConfig(ApkConfigModel apkConfigModel) {
        return apkConfigModel.getDefault();
    }

    @Singleton
    @Provides
    IApkConfigModel provideApkConfigModel(ApkConfigModel apkConfigModel) {
        return apkConfigModel;
    }

    @Singleton
    @Provides
    Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Singleton
    @Provides
    ITaskFactory provideTaskFactory(TaskFactory taskFactory) {
        return taskFactory;
    }

    @Singleton
    @Provides
    IFileEntryModel provideFileEntryModel(FileEntryModel fileEntryModel) {
        return fileEntryModel;
    }

    @Singleton
    @Provides
    IFileModelFactory provideFileModelFactory(FileModelFactory fileModelFactory) {
        return fileModelFactory;
    }
}
