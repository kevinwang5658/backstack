package com.rievo.android.library;

import android.app.Activity;
import android.view.ViewGroup;

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
    HashMap<String, LinearBackStack> backStackMap = new HashMap<>();
    HashMap<String, LinearBackStack.State> stateMap = new HashMap<>();

    BackStackManager(Activity activity) {
        this.activity = activity;

        Timber.d("New BackStackManager");
    }

    void onDestroy(){
        backStackMap = null;
    }

    public LinearBackStack createLinearBackStack(String TAG, ViewGroup container, ViewCreator firstView){
        this.createLinearBackStackCurrent("a", container, firstView);
        LinearBackStack.State state = stateMap.get(TAG);
        if (state == null){
            state = new LinearBackStack.State(TAG);
            stateMap.put(TAG, state);
        }

        LinearBackStack linearBackStack = new LinearBackStack(state, container);
        backStackMap.put(TAG, linearBackStack);

        return linearBackStack;
    }

    public LinearBackStack createLinearBackStackCurrent(String TAG, ViewGroup currentView, ViewCreator currentViewCreator){
        LinearBackStack.State state = stateMap.get(TAG);
        if (state == null){
            state = new LinearBackStack.State(TAG);
            stateMap.put(TAG, state);
        }

        LinearBackStack linearBackStack = new LinearBackStack(state, (ViewGroup) currentView.getParent());
        backStackMap.put(TAG, linearBackStack);

        return linearBackStack;
    }

    boolean goBack(){return false;}

    void setActivity(Activity activity){
        this.activity = activity;
    }

    Activity getActivity(){
        return activity;
    }
}
