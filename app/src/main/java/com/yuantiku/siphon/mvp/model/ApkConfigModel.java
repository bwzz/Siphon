package com.yuantiku.siphon.mvp.model;

import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.data.apkconfigs.ApkConfigFactory;
import com.yuantiku.siphon.data.apkconfigs.ApkType;
import com.yuantiku.siphon.mvp.imodel.IApkConfigModel;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by wanghb on 15/9/3.
 */
public class ApkConfigModel implements IApkConfigModel {

    private final ApkConfigFactory apkConfigFactory;

    private List<ApkConfig> apkConfigs;

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
        apkConfigs = apkConfigFactory.load();
        return apkConfigs;
    }

    @Override
    public void setDefault(ApkConfig apkConfig) {
        apkConfigFactory.setDefault(apkConfig);
    }

    @Override
    public ApkConfig getByIdAndType(int id, ApkType type) {
        if (apkConfigs == null || apkConfigs.isEmpty()) {
            try {
                apkConfigs = load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (apkConfigs == null) {
            return null;
        }
        for (ApkConfig apkConfig : apkConfigs) {
            if (apkConfig.getId() == id && apkConfig.getType() == type) {
                return apkConfig;
            }
        }
        return getDefault();
    }
}
