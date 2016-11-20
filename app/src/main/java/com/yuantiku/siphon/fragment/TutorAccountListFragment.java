package com.yuantiku.siphon.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yuantiku.siphon.data.FenbiAccount;
import com.yuantiku.siphon.helper.JsonHelper;
import com.yuantiku.siphon.webservice.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

import bwzz.fragment.BaseFragment;
import rx.android.schedulers.AndroidSchedulers;

import static android.R.id.list;
import static im.fir.sdk.module.c.g;
import static im.fir.sdk.module.c.l;

/**
 * Created by wanghb on 16/11/19.
 */

public class TutorAccountListFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        listView = new ListView(container.getContext());
        swipeRefreshLayout = new SwipeRefreshLayout(container.getContext());
        swipeRefreshLayout.addView(listView);
        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        return swipeRefreshLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        loadData();
    }

    private void fillListView(List<FenbiAccount> accounts) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1);
        for (FenbiAccount account : accounts) {
            arrayAdapter.add(account.getAccount());
        }
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(
                (parent, view, position, id) -> launchTutor(accounts.get(position)));
    }

    private void launchTutor(FenbiAccount account) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(
                    String.format("tutor1f6f42334e1709a4://tutor/user/login?account=%s&password=%s",
                            account.getAccount(), account.getPassword())));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "手机上没有安装猿辅导或者猿辅导没有提供支持", Toast.LENGTH_LONG).show();
        }
    }

    private void loadData() {
        ServiceFactory.createSiphonService()
                .listAccounts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((list) -> {
                    fillListView(list);
                    swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
                }, (error) -> {
                    swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
                    Toast.makeText(getActivity(), "加载失败了呀", Toast.LENGTH_SHORT).show();
                });
    }
}
