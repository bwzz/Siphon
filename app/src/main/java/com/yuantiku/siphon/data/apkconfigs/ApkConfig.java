package com.yuantiku.siphon.data.apkconfigs;

import proguard.annotation.KeepClassMemberNames;

/**
 * Created by wanghb on 15/8/23.
 */
@KeepClassMemberNames
public class ApkConfig {
    int id;
    String name;
    ApkType type;
    String icon;
    int color;
    String packageName;

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

    public String getIcon() {
        return icon;
    }

    public int getColor() {
        return color;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ApkConfig)) {
            return false;
        }
        ApkConfig oth = (ApkConfig) o;
        return oth.id == id && oth.type == type;
    }

    @Override
    public int hashCode() {
        return id ^ type.hashCode();
    }
}
