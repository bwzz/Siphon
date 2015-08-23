package com.yuantiku.siphon.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yuantiku.siphon.apkconfigs.ApkConfig;
import com.yuantiku.siphon.apkconfigs.ApkConfigFactory;
import com.yuantiku.siphon.constant.Key;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bwzz.fragment.BaseFragment;

/**
 * Created by wanghb on 15/8/23.
 */
public class AppListFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView listView = new ListView(container.getContext());
        setupListView(listView);
        return listView;
    }

    private void setupListView(ListView listView) {
        List<ApkConfig> apkConfigs = new ArrayList<>();
        try {
            List<ApkConfig> apkConfigList = ApkConfigFactory.load();
            apkConfigs.addAll(apkConfigList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return apkConfigs.size();
            }

            @Override
            public Object getItem(int position) {
                return apkConfigs.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ApkConfig apkConfig = (ApkConfig) getItem(position);
                TextView textView = new TextView(parent.getContext());
                textView.setText(apkConfig.getName() + apkConfig.getType());
                int padding = 40;
                textView.setPadding(padding, padding, padding, padding);
                return textView;
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ApkConfig apkConfig = apkConfigs.get(position);
            Gson gson = new Gson();
            Intent intent = new Intent();
            intent.putExtra(Key.ApkConfig, gson.toJson(apkConfig));
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        });
    }
}
