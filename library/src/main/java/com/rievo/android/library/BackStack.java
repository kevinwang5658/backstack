package com.rievo.android.library;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

/**
 * Created by kwang on 2017-09-13.
 */

/**
 * Singleton helper class to facilitate access to BackStackManager. It in itself is pretty useless. I just use
 * this class so that I don't have to worry about lifetimes since BackstackManager isn't a singleton, it follows
 * the lifetime of a retained fragment.
 */
public class BackStack {

    //**********************
    // Static Methods
    //**********************

    private static BackStack backStack;

    /**
     * Installs BackStack into your app. This must be called in {@link Activity#onCreate(Bundle)}
     * @param activity
     */
    public static void install(Activity activity){
        //Standard retained fragment code. This creates a new fragment if the fragment couldn't be found
        FragmentManager fm = activity.getFragmentManager();
        RetainedFragment fragment = (RetainedFragment) fm.findFragmentByTag(RetainedFragment.TAG);

        if (fragment == null){
            fragment = new RetainedFragment();
            fm.beginTransaction()
                    .add(fragment, RetainedFragment.TAG)
                    .commit();
            activity.getApplication().registerActivityLifecycleCallbacks(fragment);
            fragment.set(activity, new BackStackManager(activity));
        }

        backStack = new BackStack(fragment.getBackStackManager(
        ));
    }


    static BackStack getSelf(){
        return backStack;
    }

    public static LinearBackStack get(String TAG){
        return backStack.backStackManager.getLinearBackstack(TAG);
    }

    /**
     * Retrieves the back stack manager.
     * @return the back stack manager
     */
    public static BackStackManager getBackStackManager(){
        return backStack.backStackManager;
    }

    //*********************************
    // Instance Methods
    //*********************************

    private BackStackManager backStackManager;

    private BackStack(BackStackManager backStackManager){
        this.backStackManager = backStackManager;
    }

    void onDestroy(){
        backStackManager = null;
    }

}
