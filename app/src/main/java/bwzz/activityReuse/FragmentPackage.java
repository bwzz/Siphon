package bwzz.activityReuse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import bwzz.activity.BaseActivity;

/**
 * @author wanghb
 * @date 15/8/13.
 */
public class FragmentPackage implements Parcelable {

    public static boolean unpack(BaseActivity activity, Intent intent) {
        if (activity == null || intent == null) {
            return false;
        }
        FragmentPackage fragmentPackage = intent.getParcelableExtra(ReuseConst.fragment_package);
        if (fragmentPackage == null) {
            return false;
        }
        return fragmentPackage.unpack(activity);

    }

    private int layoutForActivity;

    private int container;

    private String fragmentClassName;

    private Bundle argument;

    public boolean unpack(BaseActivity activity) {
        if (layoutForActivity != 0) {
            activity.setContentView(layoutForActivity);
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment f = Fragment.instantiate(activity, fragmentClassName, argument);
        if (container == 0) {
            ft.add(f, null);
        } else {
            ft.add(container, f);
        }
        ft.commit();
        return true;
    }

    public FragmentPackage setLayoutForActivity(int layoutForActivity) {
        this.layoutForActivity = layoutForActivity;
        return this;
    }

    public FragmentPackage setContainer(int container) {
        this.container = container;
        return this;
    }

    public FragmentPackage setFragmentClassName(String fragmentClassName) {
        this.fragmentClassName = fragmentClassName;
        return this;
    }

    public FragmentPackage setArgument(Bundle argument) {
        this.argument = argument;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.layoutForActivity);
        dest.writeInt(this.container);
        dest.writeString(this.fragmentClassName);
        dest.writeBundle(argument);
    }

    public FragmentPackage() {}

    protected FragmentPackage(Parcel in) {
        this.layoutForActivity = in.readInt();
        this.container = in.readInt();
        this.fragmentClassName = in.readString();
        argument = in.readBundle();
    }

    public static final Creator<FragmentPackage> CREATOR = new Creator<FragmentPackage>() {
        public FragmentPackage createFromParcel(Parcel source) {
            return new FragmentPackage(source);
        }

        public FragmentPackage[] newArray(int size) {
            return new FragmentPackage[size];
        }
    };
}
