package com.yuantiku.siphon.mvp.model;

import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.data.apkconfigs.ApkConfigFactory;
import com.yuantiku.siphon.mvp.imodel.IApkConfigModel;

import java.io.IOException;
import java.util.List;

/**
 * Created by wanghb on 15/9/3.
 */
public class ApkConfigModel implements IApkConfigModel {
    public static IApkConfigModel getDefaultApkConfigModel() {
        return new ApkConfigModel();
    }

    @Override
    public ApkConfig getDefault() {
        return ApkConfigFactory.getDefault();
    }

    @Override
    public List<ApkConfig> load() throws IOException {
        return ApkConfigFactory.load();
    }

    @Override
    public void setDefault(ApkConfig apkConfig) {
        ApkConfigFactory.setDefault(apkConfig);
    }
}
