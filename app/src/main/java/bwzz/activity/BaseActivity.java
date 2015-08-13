package bwzz.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import bwzz.activityCallback.ILauncher;
import bwzz.activityCallback.LaunchArgument;
import bwzz.activityCallback.LaunchHelper;

/**
 * Created by wanghb on 15/8/12.
 */
public class BaseActivity extends AppCompatActivity implements ILauncher {
    private LaunchHelper launchHelper = new LaunchHelper(this);

    protected void launch(LaunchArgument argument) {
        launchHelper.launch(argument);
    }

    @Override
    protected final void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!launchHelper.onResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
