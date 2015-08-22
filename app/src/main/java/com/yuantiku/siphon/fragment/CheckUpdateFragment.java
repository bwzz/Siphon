package com.yuantiku.siphon.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.yuantiku.siphon.R;
import com.yuantiku.siphon.helper.ApkHelper;
import com.yuantiku.siphon.helper.AppHelper;
import com.yuantiku.siphon.helper.PathHelper;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bwzz.fragment.BaseFragment;
import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;

/**
 * Created by wanghb on 15/8/21.
 */
public class CheckUpdateFragment extends BaseFragment {
    @Bind(R.id.status)
    TextView status;

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private AppVersion appVersion;

    private Handler handler;

    private File apkFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_update, container, false);
        ButterKnife.bind(this, view);
        status.setEnabled(false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkUpdate();
        handler = new Handler((message) -> {
            status.setText(String.valueOf(message.obj));
            return true;
        });
    }

    @OnClick(R.id.icon)
    public void checkUpdate(View view) {
        checkUpdate();
    }

    @OnClick(R.id.status)
    public void downloadApk() {
        if (apkFile != null && apkFile.exists()) {
            ApkHelper.installApk(getActivity(), apkFile);
            return;
        }
        status.setEnabled(false);
        L.e("download " + appVersion.getUpdateUrl());
        String filename = String.format("%s-%s.apk", AppHelper.getAppName(getActivity()), appVersion.getVersionName());
        apkFile = PathHelper.getCacheFilePath(getActivity(), filename);
        progressWheel.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load(appVersion.getUpdateUrl())
                .progress((downloaded, total) -> {
                    String log = "下载" + (int) (downloaded * 100d / total) + "%";
                    handler.sendMessage(handler.obtainMessage(0, log));
                })
                .write(apkFile)
                .setCallback((e, file) -> {
                    if (e == null) {
                        progressWheel.setVisibility(View.GONE);
                        status.setText("下载成功，点击安装");
                        status.setEnabled(true);
                    } else {
                        if (apkFile != null) {
                            apkFile.delete();
                        }
                        status.setText("下载失败，点我重来");
                        status.setEnabled(true);
                    }
                });
    }

    private void checkUpdate() {
        status.setEnabled(false);
        FIR.checkForUpdateInFIR("24f1dc375cf795bf73d26d57fc73d17d", new VersionCheckCallback() {
            @Override
            public void onSuccess(AppVersion appVersion, boolean b) {
                CheckUpdateFragment.this.appVersion = appVersion;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(appVersion.getVersionName());
                stringBuilder.append("\n");
                stringBuilder.append(appVersion.getChangeLog());
                if (AppHelper.getVersionCode(getActivity()) <= appVersion.getVersionCode()) {
                    stringBuilder.append("\n");
                    stringBuilder.append("点我更新");
                }
                status.setText(stringBuilder);
                status.setEnabled(true);
            }

            @Override
            public void onFail(String s, int i) {
                status.setText(s);
            }

            @Override
            public void onError(Exception e) {
                status.setText(e.getMessage());
            }

            @Override
            public void onStart() {
                progressWheel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                progressWheel.postDelayed(() -> progressWheel.setVisibility(View.INVISIBLE), 1000);
            }
        });
    }
}
