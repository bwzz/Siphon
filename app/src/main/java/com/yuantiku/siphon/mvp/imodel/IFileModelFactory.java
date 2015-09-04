package com.yuantiku.siphon.mvp.imodel;

import com.yuantiku.siphon.data.FileEntry;

/**
 * Created by wanghb on 15/9/3.
 */
public interface IFileModelFactory {
    IFileModel createFileModel(String path);

    IFileModel createFileModel(FileEntry fileEntry);
}
