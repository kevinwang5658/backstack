package com.rievo.android.library;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import timber.log.Timber;

/**
 * Created by kevin on 2017-04-25.
 */

public class LifeCycleFragment extends Fragment implements Application.ActivityLifecycleCallbacks{

    private static final String TAG = "com.rievo.android.LifeCycleFragment";
    private Activity activity;
    private Class clazz;

    public LifeCycleFragment(){
        setRetainInstance(true);
    }

    static LifeCycleFragment create(Activity activity){
        FragmentManager fm = activity.getFragmentManager();
        LifeCycleFragment fragment = (LifeCycleFragment) fm.findFragmentByTag(TAG);

        if (fragment == null){
            fragment = new LifeCycleFragment();
            fm.beginTransaction()
                    .add(fragment, TAG)
                    .commit();
            activity.getApplication().registerActivityLifecycleCallbacks(fragment);
            fragment.activity = activity;
        }

        fragment.activity = activity;

        return fragment;
    }

    Activity retrieveActivity(){
        return activity;
    }

//     __________  ___   ________  __________   ________   __    ________________________  __________    ______
//    / ____/ __ \/   | / ____/  |/  / ____/ | / /_  __/  / /   /  _/ ____/ ____/ ____/\ \/ / ____/ /   / ____/
//   / /_  / /_/ / /| |/ / __/ /|_/ / __/ /  |/ / / /    / /    / // /_  / __/ / /      \  / /   / /   / __/
//  / __/ / _, _/ ___ / /_/ / /  / / /___/ /|  / / /    / /____/ // __/ / /___/ /___    / / /___/ /___/ /___
// /_/   /_/ |_/_/  |_\____/_/  /_/_____/_/ |_/ /_/    /_____/___/_/   /_____/\____/   /_/\____/_____/_____/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
//     ___   _________________    ______________  __   __    ________________________  __________    ______
//    /   | / ____/_  __/  _/ |  / /  _/_  __/\ \/ /  / /   /  _/ ____/ ____/ ____/\ \/ / ____/ /   / ____/
//   / /| |/ /     / /  / / | | / // /  / /    \  /  / /    / // /_  / __/ / /      \  / /   / /   / __/
//  / ___ / /___  / / _/ /  | |/ // /  / /     / /  / /____/ // __/ / /___/ /___    / / /___/ /___/ /___
// /_/  |_\____/ /_/ /___/  |___/___/ /_/     /_/  /_____/___/_/   /_____/\____/   /_/\____/_____/_____/

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (this.activity == null) {
            this.activity = activity;
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (this.activity == null) {
            this.activity = activity;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (this.activity == null) {
            this.activity = activity;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity == this.activity) {
            this.activity = null;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity == this.activity) {
            this.activity = null;
        }
    }
}