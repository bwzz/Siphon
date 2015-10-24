package com.yuantiku.siphon.mvp.imodel;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.interfaces.ICallback;

import java.util.List;

/**
 * Created by wanghb on 15/9/4.
 */
public interface IFileEntryModel {

    void updateAll(List<FileEntry> fileEntries, ApkConfig apkConfig);

    void list(ApkConfig apkConfig, ICallback<List<FileEntry>> callback);

    void getLatest(ApkConfig apkConfig, ICallback<FileEntry> callback);
}
