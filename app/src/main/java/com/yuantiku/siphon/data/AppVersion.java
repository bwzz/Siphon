package com.yuantiku.siphon.data;

/**
 * Created by wanghb on 16/11/20.
 */

public class AppVersion {

    private String versionShort;
    private String installUrl;
    private String changelog;
    private int build;
    private Binary binary;

    public String getVersionName() {
        return versionShort;
    }

    public String getUpdateUrl() {
        return installUrl;
    }

    public String getChangeLog() {
        return changelog;
    }

    public int getVersionCode() {
        return build;
    }

    public long getLength() {
        return binary.fsize;
    }

    private static class Binary {
        long fsize;
    }
}
