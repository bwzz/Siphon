package com.yuantiku.siphon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bwzz.activityCallback.LaunchArgument;
import bwzz.activityReuse.ContainerActivity;
import bwzz.activityReuse.FragmentPackage;
import bwzz.activityReuse.ReuseIntentBuilder;
import bwzz.fragment.BaseFragment;

import com.yuantiku.siphon.webservice.ServiceFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {

    public MainActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        int i = 0;
        if (getArguments() != null) {
            i = getArguments().getInt("i");
        }
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ((TextView) view.findViewById(R.id.text)).setText("text = " + i);
        final int finalI = i;
        view.setOnClickListener((v) -> {
            load();
            launch(finalI);
        });
        return view;
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

    private void load() {
        ServiceFactory.getService()
                .listFiles("android/102/alpha")
                .subscribe((list) -> {
                    L.i(list);
                });
    }
}
