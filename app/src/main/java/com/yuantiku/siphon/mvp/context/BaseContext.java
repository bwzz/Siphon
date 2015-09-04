package com.yuantiku.siphon.mvp.context;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.yuantiku.siphon.mvp.presenter.BasePresenter;
import com.yuantiku.siphon.mvp.presenter.IPresenterManager;

import bwzz.fragment.LifeCycleSubjectFragment;

/**
 * Created by wanghb on 15/9/3.
 */
public class BaseContext extends LifeCycleSubjectFragment implements IPresenterManager {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        createPresenters(this);
        super.onCreate(savedInstanceState);
    }

    protected void createPresenters(@NonNull IPresenterManager presenterManager) {

    }

    @Override
    public void addPresenter(BasePresenter presenter) {
        addLifeCycleObserver(presenter);
    }
}
