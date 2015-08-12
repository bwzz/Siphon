package bwzz.activityCallback;

import android.content.Intent;

/**
 * Created by wanghb on 15/8/12.
 */
public interface ILauncher {
  void startActivityForResult(Intent intent, int requestCode);
}
