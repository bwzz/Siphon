package com.yuantiku.siphon.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yuantiku.siphon.MainActivity;
import com.yuantiku.siphon.R;
import com.yuantiku.siphon.constant.Key;
import com.yuantiku.siphon.data.FenbiAccount;
import com.yuantiku.siphon.helper.JsonHelper;
import com.yuantiku.siphon.webservice.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

import bwzz.fragment.BaseFragment;
import rx.android.schedulers.AndroidSchedulers;

import static android.R.attr.id;

/**
 * Created by wanghb on 16/11/19.
 */

public class TutorAccountListFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView listView;

    private static final int LOGIN_TUTOR_NOTIFICATION_ID = 120108;

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
        setHasOptionsMenu(true);
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
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            addAccountToNotification(accounts.get(position));
            return true;
        });
    }

    private void launchTutor(FenbiAccount account) {
        try {
            Intent intent = createLoginIntent(account);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, 0, 0, "放到通知栏").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            addAccountListToNotification();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addAccountListToNotification() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(Key.FRAGMENT, TutorAccountListFragment.class.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        addNotification(intent, "猿辅导账号列表", "点击列表中的账号可快捷登录辅导", LOGIN_TUTOR_NOTIFICATION_ID);
    }

    private void addAccountToNotification(FenbiAccount account) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < account.getAccount().length(); ++i) {
            if (i == 3 || i == 5 || i == 7) {
                continue;
            }
            stringBuilder.append(account.getAccount().charAt(i));
        }
        Intent intent = createLoginIntent(account);
        addNotification(intent, account.getAccount(), "点击登录猿辅导账号 ：" + account.getAccount(),
                Integer.parseInt(stringBuilder.toString()));
        Toast.makeText(getActivity(), "账号 ：" + account.getAccount() + " 被放到了通知栏",
                Toast.LENGTH_LONG).show();
    }

    @NonNull
    private Intent createLoginIntent(FenbiAccount account) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                String.format("tutor1f6f42334e1709a4://tutor/user/login?account=%s&password=%s",
                        account.getAccount(), account.getPassword())));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        return intent;
    }

    private void addNotification(Intent intent, String title, String summary, int id) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getActivity(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap btm = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);
        Notification notification = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.tutor_icon_push)
                .setLargeIcon(btm)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(summary)
                .build();

        NotificationManager notificationManager = (NotificationManager) getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }
}
