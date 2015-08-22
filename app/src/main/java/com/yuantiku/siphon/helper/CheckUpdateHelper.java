package com.yuantiku.siphon.helper;

import android.content.*;
import android.view.View;

import com.yuantiku.siphon.app.ApplicationFactory;
import com.yuantiku.siphon.service.*;

import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;

/**
 * Created by wanghb on 15/8/22.
 */
public class CheckUpdateHelper {
    public interface CheckUpdateCallback {
        void onNewVersion(AppVersion appVersion);

        void onNoNewVersion(AppVersion appVersion);

        void onError(Exception e);
    }

    public static void checkUpdateBackgound(Context context) {
        context.startService(new Intent(context, CheckUpdateService.class));
    }

    public static void checkUpdate(CheckUpdateCallback checkUpdateCallback) {
        FIR.checkForUpdateInFIR("24f1dc375cf795bf73d26d57fc73d17d", new VersionCheckCallback() {
            @Override
            public void onSuccess(AppVersion appVersion, boolean b) {
                if (checkUpdateCallback == null) {
                    return;
                }
                if (AppHelper.getVersionCode(ApplicationFactory.getApplication()) <= appVersion.getVersionCode()) {
                    checkUpdateCallback.onNewVersion(appVersion);
                } else {
                    checkUpdateCallback.onNoNewVersion(appVersion);
                }
            }

            @Override
            public void onFail(String s, int i) {
                if (checkUpdateCallback != null) {
                    checkUpdateCallback.onError(new Exception(s));
                }
            }

            @Override
            public void onError(Exception e) {
                if (checkUpdateCallback != null) {
                    checkUpdateCallback.onError(e);
                }
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
            }
        });
    }
}
