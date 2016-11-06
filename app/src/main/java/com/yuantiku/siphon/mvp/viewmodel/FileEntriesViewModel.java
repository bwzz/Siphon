package com.yuantiku.siphon.mvp.viewmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.model.FileModelFactory;
import com.yuantiku.siphon.mvp.presenter.FileEntriesListPresenter.IView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bwzz.taskmanager.TaskException;

import static android.R.attr.entries;
import static android.R.attr.name;

/**
 * Created by wanghb on 15/9/5.
 */
public class FileEntriesViewModel extends BaseViewModel implements IView,
        OnItemClickListener, OnItemLongClickListener, TabLayout.OnTabSelectedListener {

    public interface IHandler {

        void clickFileEntry(FileEntry fileEntry);

        void longClickFileEntry(FileEntry fileEntry);

        void refresh();
    }

    enum TabTag {
        all, guess,
    };

    private IHandler handler = EmptyObjectFactory.createEmptyObject(IHandler.class);

    private SwipeRefreshLayout swipeRefreshLayout;

    private final FileEntriesAdapter adapter;

    private ListView listView;

    public FileEntriesViewModel(FileModelFactory fileModelFactory, IHandler handler) {
        adapter = new FileEntriesAdapter(fileModelFactory);
        this.handler = handler;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        TabLayout tabLayout = new TabLayout(container.getContext());
        tabLayout.addTab(tabLayout.newTab().setText("所有").setTag(TabTag.all));
        tabLayout.addTab(tabLayout.newTab().setText("猜你想要").setTag(TabTag.guess));
        tabLayout.setOnTabSelectedListener(this);
        swipeRefreshLayout = new SwipeRefreshLayout(container.getContext());
        listView = new ListView(container.getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        swipeRefreshLayout.addView(listView);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            handler.refresh();
        });
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        LinearLayout linearLayout = new LinearLayout(container.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(tabLayout);
        linearLayout.addView(swipeRefreshLayout);
        return linearLayout;
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        FileEntry fileEntry = (FileEntry) adapter.getItem(position);
        handler.longClickFileEntry(fileEntry);
        return true;
    }

    private Map<TabTag, Parcelable> listViewStateMap = new HashMap<>();

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getTag() == TabTag.all) {
            listViewStateMap.put(TabTag.guess, listView.onSaveInstanceState());
        } else {
            listViewStateMap.put(TabTag.all, listView.onSaveInstanceState());
        }
        adapter.setTabTag((TabTag) tab.getTag());
        if (listViewStateMap.get(tab.getTag()) != null) {
            listView.onRestoreInstanceState(listViewStateMap.get(tab.getTag()));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private static class FileEntriesAdapter extends BaseAdapter {

        private Map<String, View> entry2View = new HashMap<>();

        private Map<TabTag, List<FileEntry>> entriesMap = new HashMap<>();

        private final FileModelFactory fileModelFactory;

        private TabTag tabTag = TabTag.all;

        public FileEntriesAdapter(FileModelFactory fileModelFactory) {
            this.fileModelFactory = fileModelFactory;
        }

        public void setTabTag(TabTag tabTag) {
            this.tabTag = tabTag;
            entry2View.clear();
            notifyDataSetChanged();
        }

        public void update(List<FileEntry> fileEntries) {
            entriesMap.put(TabTag.all, fileEntries);
            List<FileEntry> entries = new ArrayList<>();
            Set<String> foundVersions = new HashSet<>();
            for (FileEntry entry : fileEntries) {
                String version = parseVersion(entry);
                if (TextUtils.isEmpty(version) || foundVersions.contains(version)) {
                    continue;
                }
                foundVersions.add(version);
                entries.add(entry);
            }
            entriesMap.put(TabTag.guess, entries);
            entry2View.clear();
            notifyDataSetChanged();
        }

        private String parseVersion(FileEntry entry) {
            if (entry == null) {
                return null;
            }
            String[] sps = entry.name.split("-");
            if (sps.length < 2) {
                return "";
            }
            return sps[1];
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
            if (!entriesMap.containsKey(tabTag)) {
                return 0;
            }
            return entriesMap.get(tabTag).size();
        }

        @Override
        public Object getItem(int position) {
            return entriesMap.get(tabTag).get(position);
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
            String date = fileEntry.date;
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
                date = simpleDateFormat1.format(simpleDateFormat.parse(fileEntry.date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            textView.setTextSize(18);
            textView.setText(String.format("%s\n%s", fileEntry.name, date));
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
