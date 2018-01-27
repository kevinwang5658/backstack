package com.rievo.library;

import android.app.Activity;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    private static AppCompatActivity activity;

    /**
     * Installs BackStack into your app. This must be called in {@link Activity#onCreate(Bundle)}
     * @param activity
     */
    public static void install(final AppCompatActivity activity){
        Timber.d("install backstack");

        final RetainedViewModel model = ViewModelProviders.of(activity).get(RetainedViewModel.class);
        activity.getLifecycle().addObserver(new GenericLifecycleObserver() {
            @Override
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY){
                    model.getBackStackManager().onDestroy();
                    BackStack.activity = null;
                }
            }
        });

        if (model.getBackStackManager() == null){
            Timber.d("backstack is null");
            model.setBackStackManager(new BackStackManager());
        }

        BackStack.activity = activity;
    }

    public static LinearBackStack getStack(String TAG){
        return ViewModelProviders.of(activity)
                .get(RetainedViewModel.class)
                .getBackStackManager()
                .getLinearStack(TAG);
    }

    /**
     * Retrieves the back stack manager.
     * @return the back stack manager
     */
    public static BackStackManager getBackStackManager(){
        return ViewModelProviders.of(activity)
                .get(RetainedViewModel.class)
                .getBackStackManager();
    }
}
