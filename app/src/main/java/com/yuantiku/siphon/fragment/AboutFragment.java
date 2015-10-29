package com.yuantiku.siphon.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yuantiku.siphon.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import bwzz.fragment.BaseFragment;

/**
 * @author wanghb
 * @date 15/10/29.
 */
public class AboutFragment extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.wechat)
    public void launchWeChat() {
        Toast.makeText(getActivity(), "如果你知道怎么进入微信聊天，请告诉我", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.qq)
    public void launchQQ() {
        launch("http://qm.qq.com/cgi-bin/qm/qr?k=J4LuIf8B0hP4W5hOXcPkAeH2rfbKNP6W");
    }

    private void launch(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Github").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        launch("https://github.com/bwzz/Siphon");
        return super.onOptionsItemSelected(item);
    }
}
