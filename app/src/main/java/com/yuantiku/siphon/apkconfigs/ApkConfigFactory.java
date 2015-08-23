package com.yuantiku.siphon.apkconfigs;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
        return getConfig(102, "猿辅导", ApkType.alpha);
    }

    public static ApkConfig getConfig(int id, String name, ApkType type) {
        ApkConfig apkConfig = new ApkConfig();
        apkConfig.id = id;
        apkConfig.name = name;
        apkConfig.type = type;
        return apkConfig;
    }
}
