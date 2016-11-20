package com.yuantiku.siphon.helper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import bwzz.activityCallback.LaunchArgument;
import bwzz.activityCallback.ResultCallback;
import bwzz.activityReuse.ContainerActivity;
import bwzz.activityReuse.FragmentPackage;
import bwzz.activityReuse.ReuseIntentBuilder;

/**
 * Created by wanghb on 15/9/4.
 */
public class LaunchHelper {
    public static LaunchArgument createArgument(Class<? extends Fragment> fragmentClass,
            Context context, ResultCallback callback) {
        return createArgument(fragmentClass, context, new Bundle(), callback);
    }

    public static LaunchArgument createArgument(Class<? extends Fragment> fragmentClass,
            Context context) {
        return createArgument(fragmentClass, context, new Bundle());
    }

    public static LaunchArgument createArgument(Class<? extends Fragment> fragmentClass,
            Context context, Bundle bundle) {
        return createArgument(fragmentClass, context, bundle, null);
    }

    public static LaunchArgument createArgument(Class<? extends Fragment> fragmentClass,
            Context context, Bundle bundle, ResultCallback callback) {
        return createArgument(fragmentClass.getName(), context, bundle, callback);
    }
    
    public static LaunchArgument createArgument(String fragmentClassName,
            Context context, Bundle bundle, ResultCallback callback) {
        FragmentPackage fragmentPackage = new FragmentPackage();
        fragmentPackage.setContainer(android.R.id.content)
                .setArgument(bundle)
                .setFragmentClassName(fragmentClassName);

        LaunchArgument argument = ReuseIntentBuilder.build()
                .activity(ContainerActivity.class)
                .fragmentPackage(fragmentPackage)
                .getLaunchArgumentBuilder(context)
                .callback(callback)
                .get();

        return argument;
    }
}
