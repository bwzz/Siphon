package com.yuantiku.siphon.dagger.module;

import android.app.Application;

import com.yuantiku.siphon.app.Siphon;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.mvp.model.ApkConfigModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wanghb on 15/9/7.
 */
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

    @Provides
    ApkConfig provideDefaultApkConfig(ApkConfigModel apkConfigModel) {
        return apkConfigModel.getDefault();
    }
}
