package bwzz.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.yuantiku.siphon.interfaces.ILifeCycleObserver;

/**
 * Created by wanghb on 15/9/3.
 */
public class LifeCycleSubjectFragment extends BaseFragment {
    private LifeCycleObserverGroup lifeCycleObserverGroup = new LifeCycleObserverGroup();

    protected void addLifeCycleObserver(ILifeCycleObserver iLifeCycleObserver) {
        lifeCycleObserverGroup.addLifeCycle(iLifeCycleObserver);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lifeCycleObserverGroup.onAttach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeCycleObserverGroup.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifeCycleObserverGroup.onViewCreated(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lifeCycleObserverGroup.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifeCycleObserverGroup.onStart();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        lifeCycleObserverGroup.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifeCycleObserverGroup.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lifeCycleObserverGroup.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        lifeCycleObserverGroup.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        lifeCycleObserverGroup.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        lifeCycleObserverGroup.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifeCycleObserverGroup.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        lifeCycleObserverGroup.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifeCycleObserverGroup.onDetach();
        super.onDetach();
    }
}
