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

    private int flags;

    private ReuseIntentBuilder() {
    }

    public ReuseIntentBuilder activity(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;
        return this;
    }

    public ReuseIntentBuilder fragmentPackage(FragmentPackage fragmentPackage) {
        this.fragmentPackage = fragmentPackage;
        return this;
    }

    public ReuseIntentBuilder flags(int flags) {
        this.flags = flags;
        return this;
    }

    public Intent get(Context context) {
        Intent intent = new Intent(context, activityClass);
        intent.putExtra(ReuseConst.fragment_package, fragmentPackage);
        intent.setFlags(flags);
        return intent;
    }

    public LaunchArgument.Builder getLaunchArgumentBuilder(Context context) {
        return argumentBuilder.intent(get(context));
    }
}
