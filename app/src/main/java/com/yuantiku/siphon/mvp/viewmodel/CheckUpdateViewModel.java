package com.yuantiku.siphon.mvp.viewmodel;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.yuantiku.siphon.R;
import com.yuantiku.siphon.factory.EmptyObjectFactory;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.presenter.CheckUpdatePresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.fir.sdk.version.AppVersion;

/**
 * Created by wanghb on 15/9/4.
 */
public class CheckUpdateViewModel extends BaseViewModel implements CheckUpdatePresenter.IView {

    public interface IHandler {
        void downloadApk();

        void checkUpdate();
    }

    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @Bind(R.id.icon)
    View icon;

    private Handler handler;

    private IHandler clickHandler = EmptyObjectFactory.createEmptyObject(IHandler.class);

    public CheckUpdateViewModel(IHandler clickHandler) {
        this.clickHandler = EmptyObjectFactory.ensureObject(clickHandler, IHandler.class);
        handler = new Handler((message) -> {
            status.setText(String.valueOf(message.obj));
            return true;
        });
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_update, container, false);
        ButterKnife.bind(this, view);
        status.setEnabled(false);
        return view;
    }

    @Override
    public void renderCheckStart() {
        progressWheel.setVisibility(View.VISIBLE);
        status.setEnabled(false);
        icon.setEnabled(false);
        status.setText("正在检查更新...");
    }

    @Override
    public void renderCheckFinishWithNewVersion(AppVersion appVersion) {
        showNewVersion(appVersion);
    }

    @Override
    public void renderCheckFinishWithNoNewVersion(AppVersion oldVersion) {
        finishCheck("你已经装得最新了~" + oldVersion.getVersionName(), false);
    }

    @Override
    public void renderCheckFailed(AppVersion oldVersion, Exception e) {
        finishCheck("orz，失败了，点上面的图标重试", false);
    }

    @Override
    public void renderDownloadTaskStart(AppVersion appVersion) {
        status.setEnabled(false);
        progressWheel.setVisibility(View.VISIBLE);
        String log = "下载: " + 0 + "%";
        status.setText(log);
    }

    @Override
    public void renderDownloadTaskProgress(AppVersion appVersion, float percent) {
        String log = String.format("下载中：%.2f%%", percent);
        handler.sendMessage(handler.obtainMessage(0, log));
    }

    @Override
    public void renderDownloadTaskFinished(AppVersion appVersion, IFileModel apkFile) {
        if (apkFile != null) {
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
    }

    @OnClick(R.id.status)
    protected void downloadApk() {
        clickHandler.downloadApk();
    }

    @OnClick(R.id.icon)
    protected void checkUpdate() {
        clickHandler.checkUpdate();
    }

    private void showNewVersion(AppVersion appVersion) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appVersion.getVersionName());
        stringBuilder.append("\n");
        stringBuilder.append(appVersion.getChangeLog());
        stringBuilder.append("\n");
        stringBuilder.append("点我更新");
        finishCheck(stringBuilder, true);
    }

    private void finishCheck(CharSequence info, boolean goon) {
        status.setText(info);
        status.setEnabled(goon);
        progressWheel.postDelayed(() -> {
            progressWheel.setVisibility(View.INVISIBLE);
            icon.setEnabled(true);
        }, 1000);

    }
}
