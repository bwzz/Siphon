package com.yuantiku.siphon.apkconfigs;

import android.content.*;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.yuantiku.siphon.*;
import com.yuantiku.siphon.app.*;

import org.apache.commons.io.*;

import java.io.*;
import java.util.*;

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
