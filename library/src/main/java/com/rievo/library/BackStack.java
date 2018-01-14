package com.rievo.library;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import timber.log.Timber;

/**
 * Created by kwang on 2017-09-13.
 */

/**
 * Singleton helper class to facilitate access to BackStackManager. It in itself is pretty useless. I just use
 * this class so that I don't have to worry about lifetimes since BackStackManager isn't a singleton, it follows
 * the lifetime of a retained fragment.
 */
public class BackStack {

    //This is okay because we set this to null when the activity is destroyed.
    private static RetainedFragment retainedFragment;

    /**
     * Installs BackStack into your app. This must be called in {@link Activity#onCreate(Bundle)}
     * @param activity
     */
    public static void install(Activity activity){
        Timber.d("install backstack");

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

        retainedFragment = fragment;
    }

    public static BStack getStack(String TAG){
        return retainedFragment.getBackStackManager().getStack(TAG);
    }

    public static LinearBackStack getLinearStack(String TAG){
        BStack bStack = retainedFragment.getBackStackManager().getStack(TAG);
        if (bStack instanceof LinearBackStack) {
            return (LinearBackStack) bStack;
        } else {
            return null;
        }
    }

    /**
     * Retrieves the back stack manager.
     * @return the back stack manager
     */
    public static BackStackManager getBackStackManager(){
        return retainedFragment.getBackStackManager();
    }

    public static void onDestroy(){
        retainedFragment = null;
    }

}
