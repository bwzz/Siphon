package com.yuantiku.siphon.presenter;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.helper.JsonHelper;
import com.yuantiku.siphon.model.ApkConfigModel;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.presenter.HomePresenter;
import com.yuantiku.siphon.viewmodel.HomeViewModel;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by wanghb on 15/9/3.
 */
public class HomePresenterTest {

    private static String demoConfigJson = "{\n"
            +
            "    \"id\": 102,\n"
            +
            "    \"name\": \"猿辅导\",\n"
            +
            "    \"type\": \"alpha\",\n"
            +
            "    \"icon\": \"http://group.store.qq.com/qun/V14Fy6Gm0nBKuU/V3tVxQJHItk3FXda*g4/800\",\n"
            +
            "    \"color\": -35839\n" +
            "  }";

    @Test
    public void testHomePresenter() {
        ApkConfig demoApkConfig = JsonHelper.json(demoConfigJson, ApkConfig.class);
        ApkConfigModel apkConfigModel = new ApkConfigModel();
        apkConfigModel.setDelegate(new ApkConfigModel() {
            @Override
            public ApkConfig getDefault() {
                return demoApkConfig;
            }
        });
        HomePresenter homePresenter = new HomePresenter(presenter -> {

        }, apkConfigModel);
        HomeViewModel homeViewModel = new HomeViewModel();
        homeViewModel.setDelegate(new HomeViewModel() {
            @Override
            public void renderApkConfig(ApkConfig apkConfig, FileEntry fileEntry, IFileModel apkFile) {
                assertThat(apkConfig.equals(demoApkConfig), is(true));
            }
        });

        homePresenter.onCreate(null);
        homePresenter.attachView(homeViewModel);
        homePresenter.onDestroy();
    }
}
