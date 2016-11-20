package com.yuantiku.siphon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import bwzz.activity.BaseActivity;
import bwzz.activityCallback.LaunchArgument;

import com.yuantiku.siphon.constant.Key;
import com.yuantiku.siphon.helper.CheckUpdateHelper;
import com.yuantiku.siphon.helper.LaunchHelper;
import com.yuantiku.siphon.service.WorkService;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, WorkService.class));
        CheckUpdateHelper.checkUpdateBackground(this);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String fragmentName = intent.getStringExtra(Key.FRAGMENT);
        if (TextUtils.isEmpty(fragmentName)) {
            return;
        }
        launchFragment(fragmentName);
    }

    private void launchFragment(String clazz) {
        LaunchArgument argument = LaunchHelper.createArgument(clazz, this, new Bundle(), null);
        launch(argument);
    }
}
