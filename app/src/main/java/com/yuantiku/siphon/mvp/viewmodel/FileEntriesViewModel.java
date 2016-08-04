package com.yuantiku.siphon.mvp.viewmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.model.FileModelFactory;
import com.yuantiku.siphon.mvp.presenter.FileEntriesListPresenter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bwzz.taskmanager.TaskException;

/**
 * Created by wanghb on 15/9/5.
 */
public class FileEntriesViewModel extends BaseViewModel implements FileEntriesListPresenter.IView,
        AdapterView.OnItemClickListener {

    public interface IHandler {

        void clickFileEntry(FileEntry fileEntry);

        void refresh();
    }

    private IHandler handler = EmptyObjectFactory.createEmptyObject(IHandler.class);

    private SwipeRefreshLayout swipeRefreshLayout;

    private final FileEntriesAdapter adapter;

    public FileEntriesViewModel(FileModelFactory fileModelFactory, IHandler handler) {
        adapter = new FileEntriesAdapter(fileModelFactory);
        this.handler = handler;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        swipeRefreshLayout = new SwipeRefreshLayout(container.getContext());
        ListView listView = new ListView(container.getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        swipeRefreshLayout.addView(listView);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            handler.refresh();
        });
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        return swipeRefreshLayout;
    }

    @Override
    public void renderSyncStart(ApkConfig apkConfig) {

    }

    @Override
    public void renderSyncFailed(ApkConfig apkConfig, TaskException e) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void renderSyncSuccess(ApkConfig apkConfig, List<FileEntry> fileEntries) {
        adapter.update(fileEntries);
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void renderDownloadStart(FileEntry fileEntry) {}

    @Override
    public void renderDownloadProgress(FileEntry fileEntry, float percent) {
        adapter.updateItemProgress(fileEntry, percent);
    }

    @Override
    public void renderDownloadSuccess(FileEntry fileEntry, IFileModel result) {
        adapter.updateItemDownloadSuccess(fileEntry);
    }

    @Override
    public void renderDownloadFailed(FileEntry fileEntry, TaskException e) {
        adapter.updateItemDownloadFailed(fileEntry);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileEntry fileEntry = (FileEntry) adapter.getItem(position);
        handler.clickFileEntry(fileEntry);
    }

    private static class FileEntriesAdapter extends BaseAdapter {

        private List<FileEntry> fileEntries = new LinkedList<>();

        private Map<String, View> entry2View = new HashMap<>();

        private final FileModelFactory fileModelFactory;

        public FileEntriesAdapter(FileModelFactory fileModelFactory) {
            this.fileModelFactory = fileModelFactory;
        }

        public void update(List<FileEntry> fileEntries) {
            this.fileEntries = fileEntries;
            notifyDataSetChanged();
        }

        public void updateItemProgress(FileEntry fileEntry, float progress) {
            TextView textView = getItemView(fileEntry);
            if (textView != null) {
                textView.setText(fileEntry.name + "\n" + progress);
            }
        }

        public void updateItemDownloadSuccess(FileEntry fileEntry) {
            TextView textView = getItemView(fileEntry);
            if (textView == null) {
                return;
            }
            textView.setText(fileEntry.name);
            textView.setTextColor(Color.GREEN);
        }

        public void updateItemDownloadFailed(FileEntry fileEntry) {
            TextView textView = getItemView(fileEntry);
            if (textView == null) {
                return;
            }
            textView.setText(fileEntry.name);
            textView.setTextColor(Color.RED);
        }

        private TextView getItemView(FileEntry fileEntry) {
            return (TextView) entry2View.get(fileEntry.name);
        }

        @Override
        public int getCount() {
            return fileEntries.size();
        }

        @Override
        public Object getItem(int position) {
            return fileEntries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) convertView;
            if (textView == null) {
                textView = new TextView(parent.getContext());
                textView.setMinLines(3);
            }
            FileEntry fileEntry = (FileEntry) getItem(position);
            textView.setText(String.format("%s\n%s", fileEntry.name, fileEntry.date));
            int pad = 30;
            textView.setPadding(pad, pad, pad, pad);

            IFileModel fileModel = fileModelFactory.createFileModel(fileEntry);
            textView.setTextColor(fileModel.exists() ? Color.GREEN : Color.BLACK);

            String key = (String) textView.getTag();
            if (!TextUtils.isEmpty(key)) {
                entry2View.remove(key);
            }
            textView.setTag(fileEntry.name);
            entry2View.put(fileEntry.name, textView);

            return textView;
        }
    }
}
