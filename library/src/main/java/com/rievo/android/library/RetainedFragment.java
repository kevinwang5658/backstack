package com.rievo.android.library;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by kwang on 2017-09-13.
 */

/**
 * This class is used to support BackStackManager. It keeps the object alive for the right
 * duration of time and supplies the correct activity to it.
 */
public class RetainedFragment extends Fragment implements Application.ActivityLifecycleCallbacks{

    static final String TAG = "BACKSTACK_MANAGER_FRAGMENT";

    private Activity activity;

    /**
     * This class keeps the backStackManager alive.
     */
    private BackStackManager backStackManager;

    public RetainedFragment(){
        setRetainInstance(true);
    }

    /**
     * Pseudo constructor
     * @param activity
     * @param backStackManager
     */
    void set(Activity activity, BackStackManager backStackManager){
        this.backStackManager = backStackManager;
        setActivity(activity);
    }

    void setActivity(Activity activity){
        this.activity = activity;
        backStackManager.setActivity(activity);
    }

    BackStackManager getBackStackManager(){
        return backStackManager;
    }

    //**********************
    // Activity Lifecycle
    //**********************

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        setActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        setActivity(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        setActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        setActivity(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        setActivity(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        setActivity(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        this.activity = null;
        BackStack.getSelf().onDestroy();
        backStackManager.onDestroy();
    }
}
