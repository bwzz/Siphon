package bwzz.activityCallback;

import android.content.Intent;
import android.util.SparseArray;

/**
 * Created by wanghb on 15/8/12.
 */
public class LaunchHelper {
    private ILauncher launcher;

    private SparseArray<LaunchArgument> argumentSparseArray = new SparseArray<>();

    public LaunchHelper(ILauncher launcher) {
        this.launcher = launcher;
    }

    public LaunchHelper launch(LaunchArgument argument) {
        argumentSparseArray.put(argument.getRequestCode(), argument);
        launcher.startActivityForResult(argument.getIntent(), argument.getRequestCode());
        return this;
    }

    public boolean onResult(int requestCode, int resultCode, Intent data) {
        LaunchArgument argument = argumentSparseArray.get(requestCode);
        return argument.applyCallback(resultCode, data);
    }
}
