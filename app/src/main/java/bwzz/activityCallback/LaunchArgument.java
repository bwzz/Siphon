package bwzz.activityCallback;

import android.content.Intent;

/**
 * Created by wanghb on 15/8/12.
 */
public class LaunchArgument {
  private int requestCode;
  private Intent intent;
  private ResultCallback callback;

  public int getRequestCode() {
    return requestCode;
  }

  public Intent getIntent() {
    return intent;
  }

  public boolean applyCallback(int resultCode, Intent data) {
    if (callback == null) {
      return false;
    }
    return callback.onResult(resultCode, data);
  }

  public static Builder build() {
    return new Builder();
  }

  public static class Builder {
    private LaunchArgument argument = new LaunchArgument();

    public Builder requestCode(int code) {
      argument.requestCode = code;
      return this;
    }

    public Builder intent(Intent intent) {
      argument.intent = intent;
      return this;
    }

    public Builder callback(ResultCallback callback) {
      argument.callback = callback;
      return this;
    }

    public LaunchArgument get() {
      return argument;
    }
  }

}
