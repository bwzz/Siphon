package com.yuantiku.siphon.mvp.presenter;

import com.yuantiku.siphon.interfaces.BaseLifeCycleObserver;

/**
 * Created by wanghb on 15/9/3.
 */
public class BasePresenter extends BaseLifeCycleObserver {
    public BasePresenter(IPresenterManager presenterManager) {
        presenterManager.addPresenter(this);
    }
}
