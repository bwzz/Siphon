package com.yuantiku.siphon.data.apkconfigs;

import android.app.Application;

import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.yuantiku.siphon.R;
import com.yuantiku.siphon.app.ApplicationFactory;
import com.yuantiku.siphon.helper.JsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

/**
 * Created by wanghb on 15/8/23.
 */
public class ApkConfigFactory {

    private final Application application;

    @Inject
    public ApkConfigFactory(Application application) {
        this.application = application;
    }

    public List<ApkConfig> load() throws IOException {
        InputStream inputStream = application.getResources().openRawResource(R.raw.apk_configs);
        List<ApkConfig> list = JsonHelper.jsonList(IOUtils.toString(inputStream),
                new TypeToken<List<ApkConfig>>() {
                }.getType());
        if (list == null) {
            list = new LinkedList<>();
        }
        return list;
    }

    public ApkConfig getDefault() {
        try {
            Hawk.init(application)
                    .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                    .build();
            return Hawk.get(ApkConfig.class.getName(), load().get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ApkConfig apkConfig = new ApkConfig();
        apkConfig.id = 102;
        apkConfig.name = "猿辅导";
        apkConfig.type = ApkType.alpha;
        apkConfig.icon = "http://group.store.qq.com/qun/V14Fy6Gm0nBKuU/V3tVxQJHItk3FXda*g4/800";
        apkConfig.color = -35839;
        return apkConfig;
    }

    public void setDefault(ApkConfig apkConfig) {
        Hawk.put(ApkConfig.class.getName(), apkConfig);
    }
}
