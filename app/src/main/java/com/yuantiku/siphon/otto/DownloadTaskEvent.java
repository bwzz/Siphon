package com.yuantiku.siphon.otto;

import com.yuantiku.siphon.data.FileEntry;

/**
 * Created by wanghb on 15/8/16.
 */
public class DownloadTaskEvent extends BaseEvent {
    public FileEntry fileEntry;

    public DownloadTaskEvent(FileEntry fileEntry) {
        this.fileEntry = fileEntry;
    }
}
