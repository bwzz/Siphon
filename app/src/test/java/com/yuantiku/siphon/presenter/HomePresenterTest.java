package com.yuantiku.siphon.presenter;

import com.yuantiku.siphon.dagger.component.ApplicationComponent;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.helper.JsonHelper;
import com.yuantiku.siphon.mvp.model.ApkConfigModel;
import com.yuantiku.siphon.mvp.presenter.HomePresenter;
import com.yuantiku.siphon.mvp.presenter.PresenterFactory;
import com.yuantiku.siphon.mvp.viewmodel.HomeViewModel;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        ApkConfigModel apkConfigModel = mock(ApkConfigModel.class);
        ApplicationComponent applicationComponent = mock(ApplicationComponent.class);
        PresenterFactory.initApplicationComponent(applicationComponent);
        HomePresenter homePresenter = PresenterFactory.createHomePresenter(presenter -> {});

        HomeViewModel homeViewModel = mock(HomeViewModel.class);
        // new HomeViewModel();
        // homeViewModel.setDelegate(new HomeViewModel() {
        // @Override
        // public void renderApkConfig(ApkConfig apkConfig, FileEntry fileEntry, IFileModel apkFile)
        // {
        // assertThat(apkConfig.equals(demoApkConfig), is(true));
        // }
        // });

        homePresenter.onCreate(null);
        homePresenter.attachView(homeViewModel);
        // verify(homeViewModel).renderApkConfig(any(), any(), any());
        verify(homeViewModel).renderSyncStart(any());
        homePresenter.onDestroy();
    }
}
