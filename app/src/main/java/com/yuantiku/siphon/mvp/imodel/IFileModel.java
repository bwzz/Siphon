package com.yuantiku.siphon.mvp.imodel;

/**
 * Created by wanghb on 15/9/3.
 */
public interface IFileModel {
    boolean exists();

    String getPath();

    boolean delete();
}
