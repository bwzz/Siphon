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
        int requestCode = RequestCodeGenerator.generateRequestCode(argumentSparseArray);
        argumentSparseArray.put(requestCode, argument);
        launcher.startActivityForResult(argument.getIntent(), requestCode);
        return this;
    }

    public boolean onResult(int requestCode, int resultCode, Intent data) {
        LaunchArgument argument = argumentSparseArray.get(requestCode);
        if (argument != null) {
            argumentSparseArray.remove(requestCode);
            return argument.applyCallback(resultCode, data);
        } else {
            return false;
        }
    }
}
