package com.yuantiku.siphon.apkconfigs;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.yuantiku.siphon.R;
import com.yuantiku.siphon.app.ApplicationFactory;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wanghb on 15/8/23.
 */
public class ApkConfigFactory {
    public static List<ApkConfig> load() throws IOException {
        Context context = ApplicationFactory.getApplication();
        InputStream inputStream = context.getResources().openRawResource(R.raw.apk_configs);
        Gson gson = new Gson();
        List<ApkConfig> list = gson.fromJson(IOUtils.toString(inputStream),
                new TypeToken<List<ApkConfig>>() {
                }.getType());
        if (list == null) {
            list = new LinkedList<>();
        }
        return list;
    }

    public static ApkConfig getDefault() {
        try {
            Hawk.init(ApplicationFactory.getApplication())
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

    public static void setDefault(ApkConfig apkConfig) {
        Hawk.put(ApkConfig.class.getName(), apkConfig);
    }
}
