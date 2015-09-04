package com.yuantiku.siphon.mvp.presenter;

import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.mvp.imodel.IApkConfigModel;

import java.io.IOException;
import java.util.List;

/**
 * Created by wanghb on 15/9/4.
 */
public class AppListPresenter extends BasePresenter {
    public interface IView {
        void renderList(List<ApkConfig> apkConfigs);

        void renderLoadListFailed();
    }

    private IApkConfigModel apkConfigModel;

    public AppListPresenter(IPresenterManager presenterManager, IApkConfigModel apkConfigModel) {
        super(presenterManager);
        this.apkConfigModel = apkConfigModel;
    }

    public AppListPresenter attachView(IView view) {
        try {
            List<ApkConfig> apkConfigs = apkConfigModel.load();
            view.renderList(apkConfigs);
        } catch (IOException e) {
            view.renderLoadListFailed();
        }
        return this;
    }
}
