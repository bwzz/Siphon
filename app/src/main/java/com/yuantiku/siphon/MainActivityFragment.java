package com.yuantiku.siphon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bwzz.activityCallback.LaunchArgument;
import bwzz.activityReuse.ContainerActivity;
import bwzz.activityReuse.FragmentPackage;
import bwzz.activityReuse.ReuseIntentBuilder;
import bwzz.fragment.BaseFragment;
import rx.android.schedulers.AndroidSchedulers;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.otto.BusFactory;
import com.yuantiku.siphon.otto.TaskEvent;
import com.yuantiku.siphon.otto.TaskResultEvent;
import com.yuantiku.siphon.webservice.ServiceFactory;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {
    @Bind(R.id.file_name)
    TextView fileName;

    @Bind(R.id.date)
    TextView date;

    private Bus bus = BusFactory.createBus();

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
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
    public void onTaskResultEvent(TaskResultEvent<FileEntry> taskResultEvent) {
        FileEntry fileEntry = taskResultEvent.fileEntries.get(0);
        fileName.setText(fileEntry.name);
        date.setText(fileEntry.date);
    }

    private void load() {
        BusFactory.createBus().post(new TaskEvent());
    }
}
