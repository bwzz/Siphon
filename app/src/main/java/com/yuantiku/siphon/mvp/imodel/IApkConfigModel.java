package com.yuantiku.siphon.mvp.imodel;

import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.data.apkconfigs.ApkType;

import java.io.IOException;
import java.util.List;

/**
 * Created by wanghb on 15/9/3.
 */
public interface IApkConfigModel {
    ApkConfig getDefault();

    List<ApkConfig> load() throws IOException;

    void setDefault(ApkConfig apkConfig);

    ApkConfig getByIdAndType(int id, ApkType type);
}
