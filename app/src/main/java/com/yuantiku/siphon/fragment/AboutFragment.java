package com.yuantiku.siphon.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import bwzz.fragment.BaseFragment;

import com.yuantiku.siphon.R;

/**
 * @author wanghb
 * @date 15/10/29.
 */
public class AboutFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.wechat)
    public void launchWeChat() {
        // TODO: not work
        launch("weixin://qr/BnzG3qTEw-m8reC99ykU");
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
}
