package com.yuantiku.siphon.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yuantiku.siphon.R;
import com.yuantiku.siphon.apkconfigs.ApkConfig;
import com.yuantiku.siphon.apkconfigs.ApkConfigFactory;
import com.yuantiku.siphon.constant.Key;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.helper.ApkHelper;
import com.yuantiku.siphon.helper.ZhenguanyuPathHelper;
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
import bwzz.activityCallback.LaunchArgument;
import bwzz.activityReuse.ContainerActivity;
import bwzz.activityReuse.FragmentPackage;
import bwzz.activityReuse.ReuseIntentBuilder;
import bwzz.fragment.BaseFragment;
import bwzz.taskmanager.ITask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {
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

    private Bus bus = BusFactory.getBus();

    private boolean installAuto;

    private FileEntry fileEntry;

    private File apkFile;

    private ApkConfig apkConfig = ApkConfigFactory.getDefault();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check_update:
                checkUpdate();
                break;
            case R.id.action_select_apk:
                selectApplication();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectApplication() {
        Bundle bundle = new Bundle();
        FragmentPackage fragmentPackage = new FragmentPackage();
        fragmentPackage.setContainer(android.R.id.content)
                .setArgument(bundle)
                .setFragmentClassName(AppListFragment.class.getName());

        LaunchArgument argument = ReuseIntentBuilder.build()
                .activity(ContainerActivity.class)
                .fragmentPackage(fragmentPackage)
                .getLaunchArgumentBuilder(getActivity())
                .requestCode(123)
                .callback((resultCode, data) -> {
                    if (resultCode == Activity.RESULT_OK) {
                        String acs = data.getStringExtra(Key.ApkConfig);
                        Gson gson = new Gson();
                        ApkConfig apkConfig = gson.fromJson(acs, ApkConfig.class);
                        ApkConfigFactory.setDefault(apkConfig);
                        updateApkConfig(apkConfig);
                    }
                    return true;
                })
                .get();
        launch(argument);
    }

    private void checkUpdate() {
        Bundle bundle = new Bundle();
        FragmentPackage fragmentPackage = new FragmentPackage();
        fragmentPackage.setContainer(android.R.id.content)
                .setArgument(bundle)
                .setFragmentClassName(CheckUpdateFragment.class.getName());

        LaunchArgument argument = ReuseIntentBuilder.build()
                .activity(ContainerActivity.class)
                .fragmentPackage(fragmentPackage)
                .getLaunchArgumentBuilder(getActivity())
                .get();
        launch(argument);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
        setViewStatus(true);
        // TODO: wait work service be ready, not a good idea
        icon.postDelayed(() -> updateApkConfig(apkConfig), 300);
        if (fileEntry != null) {
            showStatus("上次检测到：" + fileEntry.name + "\n" + fileEntry.date);
        }
    }

    private void updateApkConfig(ApkConfig apkConfig) {
        this.apkConfig = apkConfig;
        showStatus(apkConfig.getName() + apkConfig.getType());
        Ion.with(icon).load(apkConfig.getIcon());
        sync();
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
        return view;
    }

    @OnClick(R.id.one_step)
    public void oneStep() {
        sync();
        installAuto = true;
    }

    @OnClick(R.id.sync)
    public void sync() {
        installAuto = false;
        bus.post(new SubmitTaskEvent(TaskFactory.createSyncTask(apkConfig.getListPath())));
        setViewStatus(false);
    }

    @OnClick(R.id.download_install)
    public void downloadInstall() {
        if (apkFile != null && apkFile.exists()) {
            installApk(apkFile);
        } else if (fileEntry != null) {
            bus.post(new SubmitTaskEvent(TaskFactory.createDownloadTask(fileEntry)));
            setViewStatus(false);
        } else {
            showStatus("先同步下吧");
        }
    }

    @Subscribe
    public void onTaskStartEvent(TaskStartEvent taskStartEvent) {
        ITask task = taskStartEvent.getTask();
        if (task instanceof SyncTask) {
            SyncTask syncTask = (SyncTask) task;
            showStatus("开始同步 : " + apkConfig.getName() + apkConfig.getType());
        } else if (task instanceof DownloadApkTask) {
            DownloadApkTask downloadApkTask = (DownloadApkTask) task;
            showStatus("开始下载 : " + apkConfig.getName() + apkConfig.getType() + "\n"
                    + downloadApkTask.getID());
        }
    }

    @Subscribe
    public void onTaskProgressEvent(TaskProgressEvent taskProgressEvent) {
        showStatus(String.format("%s\n下载中：%.2f%%\n%s", apkConfig.getName() + apkConfig.getType(),
                taskProgressEvent.getPercent(), fileEntry.name));
    }

    @Subscribe
    public void onTaskFinishEvent(TaskFinishEvent taskFinishEvent) {
        ITask task = taskFinishEvent.getTask();
        if (task.getTaskException() != null) {
            setViewStatus(true);
            showStatus("请求失败\n" + task.getTaskException().getMessage());
            return;
        }
        if (task instanceof SyncTask) {
            SyncTask syncTask = (SyncTask) task;
            showStatus(String.format("同步完成 : " + apkConfig.getName() + apkConfig.getType()));
            if (syncTask.getResult() == null || syncTask.getResult().isEmpty()) {
                showStatus(apkConfig.getName() + apkConfig.getType() + "\n没有发现安装包");
                setViewStatus(true);
            } else {
                fileEntry = syncTask.getResult().get(0);
                showStatus(String.format("同步完成 : %s\n%s\n%s",
                        apkConfig.getName() + apkConfig.getType(), fileEntry.name,
                        fileEntry.date));
                File file = new File(ZhenguanyuPathHelper.createCachePath(fileEntry));
                if (file.exists()) {
                    apkFile = file;
                } else {
                    apkFile = null;
                }

                if (installAuto) {
                    if (apkFile == null) {
                        bus.post(new SubmitTaskEvent(TaskFactory.createDownloadTask(fileEntry)));
                    } else {
                        setViewStatus(true);
                        installApk(apkFile);
                    }
                } else {
                    setViewStatus(true);
                }
            }
        } else if (task instanceof DownloadApkTask) {
            DownloadApkTask downloadApkTask = (DownloadApkTask) task;
            apkFile = downloadApkTask.getResult();
            setViewStatus(true);
            showStatus("下载完成 : " + apkConfig.getName() + apkConfig.getType() + "\n"
                    + downloadApkTask.getResult());
            installApk(apkFile);
        }
    }

    private void setViewStatus(boolean free) {
        downloadInstall.setEnabled(fileEntry != null && free);
        sync.setEnabled(free);
        oneStep.setEnabled(free);
        progressWheel.setVisibility(free ? View.INVISIBLE : View.VISIBLE);
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
