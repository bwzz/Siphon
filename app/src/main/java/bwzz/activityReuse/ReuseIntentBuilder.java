package bwzz.activityReuse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import bwzz.activityCallback.LaunchArgument;

/**
 * @author wanghb
 * @date 15/8/13.
 */
public class ReuseIntentBuilder {
    public static ReuseIntentBuilder build() {
        return new ReuseIntentBuilder();
    }

    private Class<? extends Activity> activityClass;

    private FragmentPackage fragmentPackage;

    private LaunchArgument.Builder argumentBuilder = new LaunchArgument.Builder();

    private ReuseIntentBuilder() {}

    public ReuseIntentBuilder activty(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;
        return this;
    }

    public ReuseIntentBuilder fragmentPackage(FragmentPackage fragmentPackage) {
        this.fragmentPackage = fragmentPackage;
        return this;
    }

    public Intent get(Context context) {
        Intent intent = new Intent(context, activityClass);
        intent.putExtra(ReuseConst.fragment_package, fragmentPackage);
        return intent;
    }

    public LaunchArgument.Builder getLaunchArgumentBuiler(Context context) {
        return argumentBuilder.intent(get(context));
    }
}
