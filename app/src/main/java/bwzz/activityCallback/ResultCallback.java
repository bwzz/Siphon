package bwzz.activityCallback;

import android.content.Intent;

/**
 * Created by wanghb on 15/8/12.
 */
public interface ResultCallback {
    boolean onResult(int resultCode, Intent data);
}
