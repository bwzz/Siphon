package com.yuantiku.siphon.helper;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wanghb on 15/9/4.
 */
public class JsonHelper {
    public static String json(Object data) {
        return getGson().toJson(data);
    }

    public static <T> T json(String string, Class<T> clazz) {
        return getGson().fromJson(string, clazz);
    }

    public static <T> T json(String string, Type type) {
        return getGson().fromJson(string, type);
    }

    public static <T> List<T> jsonList(String string, Type type) {
        return getGson().fromJson(string, type);
    }

    @NonNull
    private static Gson getGson() {
        return new Gson();
    }
}
