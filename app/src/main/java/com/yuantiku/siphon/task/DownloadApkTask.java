package com.yuantiku.siphon.task;

import android.content.Context;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.helper.ZhenguanyuPathHelper;
import com.yuantiku.siphon.mvp.imodel.IFileModelFactory;

/**
 * Created by wanghb on 15/8/20.
 */
public class DownloadApkTask extends DownloadTask {

    private FileEntry fileEntry;

    DownloadApkTask(IFileModelFactory fileModelFactory, Context context, FileEntry fileEntry) {
        super(fileModelFactory, context, ZhenguanyuPathHelper.create(fileEntry),
                ZhenguanyuPathHelper
                        .createCachePath(fileEntry));
        this.fileEntry = fileEntry;
    }

    public FileEntry getFileEntry() {
        return fileEntry;
    }
}
