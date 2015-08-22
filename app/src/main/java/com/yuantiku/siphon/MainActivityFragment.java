package com.yuantiku.siphon;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.helper.ApkHelper;
import com.yuantiku.siphon.otto.BusFactory;
import com.yuantiku.siphon.otto.taskevent.SubmitTaskEvent;
import com.yuantiku.siphon.otto.taskevent.TaskFinishEvent;
import com.yuantiku.siphon.otto.taskevent.TaskProgressEvent;
import com.yuantiku.siphon.otto.taskevent.TaskStartEvent;
import com.yuantiku.siphon.task.DownloadApkTask;
import com.yuantiku.siphon.task.SyncTask;
import com.yuantiku.siphon.task.TaskFactory;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bwzz.fragment.BaseFragment;
import bwzz.taskmanager.ITask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {
    @Bind(R.id.sync)
    View sync;

    @Bind(R.id.sync_install)
    View syncInstall;

    @Bind(R.id.install)
    View install;

    @Bind(R.id.status)
    TextView status;

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private Bus bus = BusFactory.getBus();

    private boolean installAuto;

    private FileEntry fileEntry;

    private File apkFile;

    private Handler handler;

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
        install.setEnabled(apkFile != null && apkFile.exists());
    }

    @Override
    public void onPause() {
        bus.unregister(this);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        handler = new Handler(msg -> {
            showStatus((String) msg.obj);
            return true;
        });
        return view;
    }

    @OnClick(R.id.sync_install)
    public void syncInstall(View view) {
        installAuto = true;
        sync(sync);
    }

    @OnClick(R.id.sync)
    public void sync(View view) {
        bus.post(new SubmitTaskEvent(TaskFactory.createSyncTask("android/102/alpha")));
        view.setEnabled(false);
        syncInstall.setEnabled(false);
        progressWheel.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.install)
    public void install(View view) {
        if (apkFile != null && apkFile.exists()) {
            installApk(apkFile);
        } else {
            showStatus("先同步下吧");
        }
    }


    @Subscribe
    public void onTaskStartEvent(TaskStartEvent taskStartEvent) {
        ITask task = taskStartEvent.getTask();
        if (task instanceof SyncTask) {
            SyncTask syncTask = (SyncTask) task;
            showStatus("开始同步 : " + syncTask.getID());
        } else if (task instanceof DownloadApkTask) {
            DownloadApkTask downloadApkTask = (DownloadApkTask) task;
            showStatus("开始下载 : " + downloadApkTask.getID());
        }
    }

    @Subscribe
    public void onTaskProgressEvent(TaskProgressEvent taskProgressEvent) {
        handler.sendMessage(handler.obtainMessage(0, String.format("下载中：%.2f%%", taskProgressEvent.getPercent())));
    }

    @Subscribe
    public void onTaskFinishEvent(TaskFinishEvent taskFinishEvent) {
        ITask task = taskFinishEvent.getTask();
        if (task instanceof SyncTask) {
            SyncTask syncTask = (SyncTask) task;
            showStatus("同步完成 : " + syncTask.getID());
            FileEntry fileEntry = syncTask.getResult().get(0);
            bus.post(new SubmitTaskEvent(TaskFactory.createDownloadTask(fileEntry)));
        } else if (task instanceof DownloadApkTask) {
            DownloadApkTask downloadApkTask = (DownloadApkTask) task;
            apkFile = downloadApkTask.getResult();
            install.setEnabled(true);
            showStatus("下载完成 : " + downloadApkTask.getResult());
            if (installAuto) {
                installAuto = false;
                installApk(apkFile);
            }
            sync.setEnabled(true);
            install.setEnabled(true);
            syncInstall.setEnabled(true);
            progressWheel.setVisibility(View.INVISIBLE);
        }
    }

    private void showStatus(String status) {
        this.status.setText(status);
    }

    private void installApk(File apkFile) {
        try {
            ApkHelper.installApk(getActivity(), apkFile);
        } catch (Exception e) {
            showStatus("安转失败了，是人品问题。");
        }
    }

}
