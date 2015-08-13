package bwzz.activityReuse;

import android.os.Bundle;

import bwzz.activity.BaseActivity;

/**
 * @author wanghb
 * @date 15/8/13.
 */
public class ContainerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentPackage.unpack(this, getIntent());
    }
}
