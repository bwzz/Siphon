package com.yuantiku.siphon.model;

import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.mvp.imodel.IApkConfigModel;

import java.io.IOException;
import java.util.List;

/**
 * Created by wanghb on 15/9/3.
 */
public class ApkConfigModel implements IApkConfigModel {

    private IApkConfigModel delegate;

    public void setDelegate(IApkConfigModel delegate) {
        this.delegate = delegate;
    }

    public ApkConfig getDefault() {
        return delegate.getDefault();
    }

    public List<ApkConfig> load() throws IOException {
        return delegate.load();
    }

    public void setDefault(ApkConfig apkConfig) {
        delegate.setDefault(apkConfig);
    }

}
