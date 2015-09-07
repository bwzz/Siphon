package com.yuantiku.siphon.mvp.model;

import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.data.apkconfigs.ApkConfigFactory;
import com.yuantiku.siphon.mvp.imodel.IApkConfigModel;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by wanghb on 15/9/3.
 */
public class ApkConfigModel implements IApkConfigModel {

    private final ApkConfigFactory apkConfigFactory;

    @Inject
    public ApkConfigModel(ApkConfigFactory apkConfigFactory) {
        this.apkConfigFactory = apkConfigFactory;
    }

    @Override
    public ApkConfig getDefault() {
        return apkConfigFactory.getDefault();
    }

    @Override
    public List<ApkConfig> load() throws IOException {
        return apkConfigFactory.load();
    }

    @Override
    public void setDefault(ApkConfig apkConfig) {
        apkConfigFactory.setDefault(apkConfig);
    }
}
