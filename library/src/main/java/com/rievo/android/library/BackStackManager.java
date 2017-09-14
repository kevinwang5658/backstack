package com.rievo.android.library;

import android.app.Activity;

import java.util.HashMap;

import timber.log.Timber;

/**
 * Created by kwang on 2017-09-13.
 */

/**
 * This is the heart of the library. The lifetime of this class should already be taken care of. This class
 * is created when the app is first created and ends the app ends (when isFinish is true not when the process
 * is destroyed). The lifetime should be managed by BackStack and Retained Fragment. This object should be available
 * as soon as BackStack#install(Activity) is called and should be available in an activity from anytime then on except
 * for onDestroy.
 */
public class BackStackManager {

    private Activity activity;
    HashMap<String, Reversible> backStackMap = new HashMap<>();

    BackStackManager(Activity activity){
        this.activity = activity;

        Timber.d("New BackStackManager");
    }

    void setActivity(Activity activity){
        this.activity = activity;
    }

    Activity getActivity(){
        return activity;
    }

    boolean goBack(){return false;}
}
