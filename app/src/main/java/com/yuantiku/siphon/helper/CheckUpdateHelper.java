package com.yuantiku.siphon.helper;

import android.content.Context;
import android.content.Intent;

import com.yuantiku.siphon.R;
import com.yuantiku.siphon.data.AppVersion;
import com.yuantiku.siphon.service.CheckUpdateService;
import com.yuantiku.siphon.webservice.ServiceFactory;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanghb on 15/8/22.
 */
public class CheckUpdateHelper {
    public interface CheckUpdateCallback {
        void onNewVersion(AppVersion appVersion);

        void onNoNewVersion(AppVersion appVersion);

        void onError(Throwable e);
    }

    public static void checkUpdateBackground(Context context) {
        context.startService(new Intent(context, CheckUpdateService.class));
    }

    public static void checkUpdate(Context context, CheckUpdateCallback checkUpdateCallback) {
        ServiceFactory.createFirService().getAppVersion(AppHelper.getPackageName(context),
                context.getString(R.string.fir_api_token), "android")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((appVersion) -> {
                    if (checkUpdateCallback == null) {
                        return;
                    }
                    if (AppHelper.getVersionCode(context) < appVersion
                            .getVersionCode()) {
                        checkUpdateCallback.onNewVersion(appVersion);
                    } else {
                        checkUpdateCallback.onNoNewVersion(appVersion);
                    }
                }, (error) -> {
                    if (checkUpdateCallback == null) {
                        return;
                    }
                    checkUpdateCallback.onError(error);
                });
    }
}
