package com.yuantiku.siphon.app;

import com.yuantiku.siphon.dagger.component.ApplicationComponent;

/**
 * Created by wanghb on 15/9/7.
 */
public class ApplicationComponentProvider {
    public static ApplicationComponent getApplicationComponent() {
        if (Siphon.application == null) {
            return null;
        }
        return Siphon.application.getApplicationComponent();
    }
}
