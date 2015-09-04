package com.yuantiku.siphon.interfaces;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by wanghb on 15/9/3.
 */
public interface ILifeCycleObserver {

    void onAttach();

    void onCreate(@Nullable Bundle savedInstanceState);

    void onViewCreated(@Nullable Bundle savedInstanceState);

    void onActivityCreated(@Nullable Bundle savedInstanceState);

    void onViewStateRestored(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onSaveInstanceState(Bundle outState);

    void onConfigurationChanged(Configuration newConfig);

    void onPause();

    void onStop();

    void onDestroyView();

    void onDestroy();

    void onDetach();
}
