package com.yuantiku.siphon.task;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.helper.ZhenguanyuPathHelper;

/**
 * Created by wanghb on 15/8/20.
 */
public class DownloadApkTask extends DownloadTask {

    DownloadApkTask(FileEntry fileEntry) {
        super(ZhenguanyuPathHelper.create(fileEntry), ZhenguanyuPathHelper.createCachePath(fileEntry));
    }
}
