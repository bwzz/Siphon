package com.yuantiku.siphon.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.yuantiku.siphon.R;
import com.yuantiku.siphon.constant.Key;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.mvp.imodel.IApkConfigModel;
import com.yuantiku.siphon.mvp.model.ApkConfigModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import bwzz.fragment.BaseFragment;

/**
 * Created by wanghb on 15/8/23.
 */
public class AppListFragment extends BaseFragment {
    private LayoutInflater inflater;

    private IApkConfigModel apkConfigModel = ApkConfigModel.getDefaultApkConfigModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        GridView listView = new GridView(container.getContext());
        listView.setNumColumns(3);
        setupListView(listView);
        return listView;
    }

    private void setupListView(GridView listView) {
        List<ApkConfig> apkConfigs = new ArrayList<>();
        try {
            List<ApkConfig> apkConfigList = apkConfigModel.load();
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
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.apk_item, parent, false);
                    ViewHolder viewHolder = new ViewHolder();
                    convertView.setTag(viewHolder);
                    ButterKnife.bind(viewHolder, convertView);
                }
                ViewHolder viewHolder = (ViewHolder) convertView.getTag();
                Ion.with(viewHolder.icon)
                        .load(apkConfig.getIcon());
                viewHolder.type.setText(apkConfig.getType().toString());
                return convertView;
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

    public static class ViewHolder {
        @Bind(R.id.icon)
        public ImageView icon;
        @Bind(R.id.type)
        public TextView type;
    }
}
