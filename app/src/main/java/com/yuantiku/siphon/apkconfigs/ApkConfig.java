package com.yuantiku.siphon.apkconfigs;

/**
 * Created by wanghb on 15/8/23.
 */
public class ApkConfig {
    int id;
    String name;
    ApkType type;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ApkType getType() {
        return type;
    }

    public String getListPath() {
        return String.format("android/%d/%s", id, type.name());
    }
}
