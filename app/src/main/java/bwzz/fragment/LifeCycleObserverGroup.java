package bwzz.fragment;

import android.content.res.Configuration;
import android.os.Bundle;

import com.yuantiku.siphon.interfaces.BaseLifeCycleObserver;
import com.yuantiku.siphon.interfaces.ILifeCycleObserver;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wanghb on 15/9/3.
 */
public class LifeCycleObserverGroup extends BaseLifeCycleObserver {
    private List<ILifeCycleObserver> lifeCycles = new LinkedList<>();

    public void addLifeCycle(ILifeCycleObserver lifeCycle) {
        if (!lifeCycles.contains(lifeCycle)) {
            lifeCycles.add(lifeCycle);
        }
    }

    @Override
    public void onAttach() {
        super.onAttach();
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onAttach();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onViewCreated(savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onStart();
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onViewStateRestored(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onResume();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onPause() {
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onStop();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onDestroyView();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        for (ILifeCycleObserver lifeCycle : lifeCycles) {
            lifeCycle.onDetach();
        }
        super.onDetach();
    }
}
