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

    public LifeCycleFragment(){
        setRetainInstance(true);
    }

    public static LifeCycleFragment create(Activity activity){
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


        return fragment;
    }

    public Activity retrieveActivity(){
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
        Timber.d("fragment onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("fragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("fragment onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.d("fragment onDestroyView");
    }
//     ___   _________________    ______________  __   __    ________________________  __________    ______
//    /   | / ____/_  __/  _/ |  / /  _/_  __/\ \/ /  / /   /  _/ ____/ ____/ ____/\ \/ / ____/ /   / ____/
//   / /| |/ /     / /  / / | | / // /  / /    \  /  / /    / // /_  / __/ / /      \  / /   / /   / __/
//  / ___ / /___  / / _/ /  | |/ // /  / /     / /  / /____/ // __/ / /___/ /___    / / /___/ /___/ /___
// /_/  |_\____/ /_/ /___/  |___/___/ /_/     /_/  /_____/___/_/   /_____/\____/   /_/\____/_____/_____/

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Timber.d("activity onCreate");
        if (this.activity == null) {
            this.activity = activity;
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Timber.d("activity onStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Timber.d("activity onResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Timber.d("activity onPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Timber.d("activity onStopped");
        this.activity = null;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Timber.d("activity onSaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Timber.d("activity onDestroyed");
        this.activity = null;
    }
}
