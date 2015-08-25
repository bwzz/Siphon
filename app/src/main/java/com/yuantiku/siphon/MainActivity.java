package com.yuantiku.siphon;

import android.content.Intent;
import android.os.Bundle;

import bwzz.activity.BaseActivity;

import com.yuantiku.siphon.helper.CheckUpdateHelper;
import com.yuantiku.siphon.service.WorkService;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, WorkService.class));
        CheckUpdateHelper.checkUpdateBackgound(this);
    }

}
