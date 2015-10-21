package com.yuantiku.siphon.mvp.viewmodel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.yuantiku.siphon.R;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.presenter.HomePresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bwzz.taskmanager.TaskException;

/**
 * Created by wanghb on 15/9/3.
 */
public class HomeViewModel extends BaseViewModel implements HomePresenter.IView {

    public interface IHandler {
        void onOneStep();

        void onSync();

        void onDownloadAndInstall();

        void onIcon(ApkConfig apkConfig);
    }

    @Bind(R.id.sync)
    View sync;

    @Bind(R.id.one_step)
    View oneStep;

    @Bind(R.id.download_install)
    View downloadInstall;

    @Bind(R.id.status)
    TextView status;

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @Bind(R.id.icon)
    ImageView icon;

    private IHandler handler;

    public HomeViewModel(View rootView, IHandler handler) {
        ButterKnife.bind(this, rootView);
        this.handler = EmptyObjectFactory.ensureObject(handler, IHandler.class);
    }

    @Override
    public void renderApkConfig(ApkConfig apkConfig, FileEntry fileEntry, IFileModel apkFile) {
        showStatus(apkConfig.getName() + apkConfig.getType());
        Ion.with(icon).load(apkConfig.getIcon());
        progressWheel.setBarColor(apkConfig.getColor());
        setViewStatus(true);
        downloadInstall.setEnabled(fileEntry != null);
        if (fileEntry != null) {
            showStatus(String.format("同步完成 : %s\n%s\n%s",
                    apkConfig.getName() + apkConfig.getType(), fileEntry.name,
                    fileEntry.date));
        }
        icon.setOnClickListener(v -> handler.onIcon(apkConfig));
    }

    @Override
    public void renderSyncStart(ApkConfig apkConfig) {
        showStatus("开始同步 : " + apkConfig.getName() + apkConfig.getType());
        setViewStatus(false);
    }

    @Override
    public void renderSyncFailed(ApkConfig apkConfig, TaskException e) {
        showStatus("请求失败\n" + e.getMessage());
        setViewStatus(true);
    }

    @Override
    public void renderSyncSuccess(ApkConfig apkConfig, List<FileEntry> fileEntries) {
        showStatus(String.format("同步完成 : " + apkConfig.getName() + apkConfig.getType()));
        setViewStatus(true);
        if (fileEntries == null || fileEntries.isEmpty()) {
            showStatus(apkConfig.getName() + apkConfig.getType() + "\n没有发现安装包");
            setViewStatus(true);
        } else {
            FileEntry fileEntry = fileEntries.get(0);
            showStatus(String.format("同步完成 : %s\n%s\n%s",
                    apkConfig.getName() + apkConfig.getType(), fileEntry.name,
                    fileEntry.date));
        }
    }

    @Override
    public void renderDownloadStart(FileEntry fileEntry) {
        setViewStatus(false);
        ApkConfig apkConfig = fileEntry.getApkConfig();
        showStatus("开始下载 : " + apkConfig.getName() + apkConfig.getType());
    }

    @Override
    public void renderDownloadProgress(FileEntry fileEntry, float percent) {
        ApkConfig apkConfig = fileEntry.getApkConfig();
        showStatus(String.format("%s\n下载中：%.2f%%\n%s", apkConfig.getName() + apkConfig.getType(),
                percent, fileEntry.name));
    }

    @Override
    public void renderDownloadSuccess(FileEntry fileEntry, IFileModel result) {
        ApkConfig apkConfig = fileEntry.getApkConfig();
        showStatus("下载完成 : " + apkConfig.getName() + apkConfig.getType() + "\n"
                + result);
        setViewStatus(true);
    }

    @Override
    public void renderDownloadFailed(FileEntry fileEntry, TaskException e) {
        showStatus("请求失败\n" + e.getMessage());
        setViewStatus(true);
    }

    private void setViewStatus(boolean free) {
        downloadInstall.setEnabled(free);
        sync.setEnabled(free);
        oneStep.setEnabled(free);
        progressWheel.setVisibility(free ? View.INVISIBLE : View.VISIBLE);
    }

    private void showStatus(String status) {
        this.status.setText(status);
    }

    @OnClick({
            R.id.one_step, R.id.sync, R.id.download_install
    })
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.one_step:
                handler.onOneStep();
                break;
            case R.id.sync:
                handler.onSync();
                break;
            case R.id.download_install:
                handler.onDownloadAndInstall();
                break;
        }
    }
}
