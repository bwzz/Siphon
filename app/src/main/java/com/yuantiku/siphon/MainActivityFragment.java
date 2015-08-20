package com.yuantiku.siphon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.otto.BusFactory;
import com.yuantiku.siphon.otto.taskevent.SubmitTaskEvent;
import com.yuantiku.siphon.otto.taskevent.TaskFinishEvent;
import com.yuantiku.siphon.otto.taskevent.TaskStartEvent;
import com.yuantiku.siphon.task.DownloadTask;
import com.yuantiku.siphon.task.SyncTask;
import com.yuantiku.siphon.task.TaskFactory;

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
    @Bind(R.id.file_name)
    TextView fileName;

    @Bind(R.id.date)
    TextView date;

    @Bind(R.id.status)
    TextView status;

    private Bus bus = BusFactory.getBus();


    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        bus.unregister(this);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int i = 0;
        if (getArguments() != null) {
            i = getArguments().getInt("i");
        }
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        fileName.setText("Nmae");
        date.setText("Date");
        return view;
    }

    @OnClick(R.id.file_item)
    public void refresh(View view) {
        load();
    }

    private void launch(int finalI) {
        Bundle bundle = new Bundle();
        bundle.putInt("i", finalI + 1);
        FragmentPackage fragmentPackage = new FragmentPackage();
        fragmentPackage.setContainer(android.R.id.content)
                .setArgument(bundle)
                .setFragmentClassName(MainActivityFragment.class.getName());

        LaunchArgument argument = ReuseIntentBuilder.build()
                .activty(ContainerActivity.class)
                .fragmentPackage(fragmentPackage)
                .getLaunchArgumentBuiler(getActivity())
                .requestCode(123)
                .callback((resultCode, data) -> {
                    L.e("", "resultCode " + resultCode, data);
                    return false;
                }).get();
        launch(argument);
    }

    @Subscribe
    public void onTaskFinishEvent(TaskFinishEvent taskFinishEvent) {
        ITask task = taskFinishEvent.getTask();
        if (task instanceof SyncTask) {
            SyncTask syncTask = (SyncTask) task;
            showStatus("finish sync : " + syncTask.getID());
            FileEntry fileEntry = syncTask.getFileEntries().get(0);
            fileName.setText(fileEntry.name);
            date.setText(fileEntry.date);
            bus.post(new SubmitTaskEvent(TaskFactory.createDownloadTask(fileEntry)));
        } else if (task instanceof DownloadTask) {
            DownloadTask downloadTask = (DownloadTask) task;
            showStatus("download finished : " + downloadTask.getTargetFile());
        }
    }

    @Subscribe
    public void onTaskStartEvent(TaskStartEvent taskStartEvent) {
        ITask task = taskStartEvent.getTask();
        if (task instanceof SyncTask) {
            SyncTask syncTask = (SyncTask) task;
            showStatus("start sync : " + syncTask.getID());
        } else if (task instanceof DownloadTask) {
            DownloadTask downloadTask = (DownloadTask) task;
            showStatus("start download : " + downloadTask.getID());
        }
    }

    private void showStatus(String status) {
        this.status.setText(status);
    }

    private void load() {
        bus.post(new SubmitTaskEvent(TaskFactory.createSyncTask("android/102/alpha")));
    }
}
