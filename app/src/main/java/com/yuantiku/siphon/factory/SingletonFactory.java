package com.yuantiku.siphon.factory;

import com.yuantiku.siphon.dagger.component.ApplicationComponent;
import com.yuantiku.siphon.mvp.imodel.IApkConfigModel;

import javax.inject.Inject;

/**
 * Created by wanghb on 15/10/11.
 */
public class SingletonFactory {
    private static SingletonFactory singletonFactory;

    public static SingletonFactory get() {
        return singletonFactory;
    }

    public static void init(ApplicationComponent applicationComponent) {
        if (singletonFactory == null) {
            singletonFactory = new SingletonFactory();
        }
        applicationComponent.inject(singletonFactory);
    }

    private SingletonFactory() {}

    @Inject
    IApkConfigModel apkConfigModel;

    public IApkConfigModel getApkConfigModel() {
        return apkConfigModel;
    }
}
