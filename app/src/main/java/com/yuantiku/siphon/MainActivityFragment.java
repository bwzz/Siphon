package com.yuantiku.siphon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import bwzz.activityCallback.LaunchArgument;
import bwzz.activityCallback.ResultCallback;
import bwzz.activityReuse.ContainerActivity;
import bwzz.activityReuse.FragmentPackage;
import bwzz.activityReuse.ReuseIntentBuilder;
import bwzz.fragment.BaseFragment;

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
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        .callback(new ResultCallback() {
                            @Override
                            public boolean onResult(int resultCode, Intent data) {
                                Log.e("", "resultCode " + resultCode);
                                return false;
                            }
                        }).get();
                launch(argument);
            }
        });
        return view;
    }
}
