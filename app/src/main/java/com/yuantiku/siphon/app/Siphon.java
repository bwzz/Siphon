package com.yuantiku.siphon.app;

import android.app.Application;

import com.yuantiku.siphon.BuildConfig;
import com.yuantiku.siphon.dagger.component.ApplicationComponent;
import com.yuantiku.siphon.dagger.component.DaggerApplicationComponent;
import com.yuantiku.siphon.dagger.module.ApplicationModule;
import com.yuantiku.siphon.factory.SingletonFactory;
import com.yuantiku.siphon.helper.SprinklesHelper;
import com.yuantiku.siphon.mvp.presenter.PresenterFactory;

import im.fir.sdk.FIR;

/**
 * Created by wanghb on 15/8/21.
 */
public class Siphon extends Application {
    static Siphon application;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        application = this;
        super.onCreate();
        FIR.init(this);
        FIR.setDebug(BuildConfig.DEBUG);
        this.initializeInjector();
        PresenterFactory.initApplicationComponent(applicationComponent);
        SingletonFactory.init(applicationComponent);
        SprinklesHelper.init(this);
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

}
