package com.yuantiku.siphon.otto;

import com.yuantiku.siphon.data.FileEntry;

import java.util.List;

/**
 * Created by wanghb on 15/8/16.
 */
public class TaskResultEvent<T> extends BaseEvent {
    public List<T> fileEntries;
}
