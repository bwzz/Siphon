package com.yuantiku.siphon.mvp.viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.yuantiku.siphon.R;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.mvp.presenter.AppListPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wanghb on 15/9/4.
 */
public class AppListViewModel extends BaseViewModel implements AppListPresenter.IView {

    public interface IHandler {
        void onAppSelected(ApkConfig apkConfig);
    }

    private AbsListView listView;
    private LayoutInflater inflater;

    private IHandler handler;

    public AppListViewModel(IHandler handler) {
        this.handler = handler;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GridView listView = new GridView(container.getContext());
        listView.setNumColumns(3);
        this.listView = listView;
        this.inflater = inflater;
        return listView;
    }

    @Override
    public void renderList(List<ApkConfig> apkConfigs) {
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
            handler.onAppSelected(apkConfig);
        });
    }

    @Override
    public void renderLoadListFailed() {
        // TODO
    }

    public static class ViewHolder {
        @Bind(R.id.icon)
        public ImageView icon;
        @Bind(R.id.type)
        public TextView type;
    }
}
