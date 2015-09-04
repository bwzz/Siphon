package com.yuantiku.siphon.mvp.imodel;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;

import java.util.List;

/**
 * Created by wanghb on 15/9/4.
 */
public interface IFileEntryModel {

    void updateAll(List<FileEntry> fileEntries, ApkConfig apkConfig);

    List<FileEntry> list(ApkConfig apkConfig);

    FileEntry getLatest(ApkConfig apkConfig);
}
