package com.rievo.android.library;

import android.app.Activity;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
        backStackMap.clear();
    }

    public LinearBackStack createLinearBackStack(String TAG, ViewGroup container, ViewCreator firstView){
        LinearBackStack.State state = retrieveOrCreateState(TAG, firstView);
        LinearBackStack linearBackStack = new LinearBackStack(state, container, activity);
        backStackMap.put(TAG, linearBackStack);
        linearBackStack.init();

        return linearBackStack;
    }

    public LinearBackStack createLinearBackStackCurrent(String TAG, ViewGroup currentView, ViewCreator currentViewCreator){
        LinearBackStack.State state = retrieveOrCreateState(TAG, currentViewCreator);
        LinearBackStack linearBackStack = new LinearBackStack(state, (ViewGroup) currentView.getParent(), activity);
        backStackMap.put(TAG, linearBackStack);

        return linearBackStack;
    }

    private LinearBackStack.State retrieveOrCreateState(String TAG, ViewCreator firstView){
        LinearBackStack.State state = stateMap.get(TAG);
        if (state == null){
            BackStackNode backStackNode = new BackStackNode(firstView);
            state = new LinearBackStack.State(TAG, backStackNode);
            stateMap.put(TAG, state);
        }

        return state;
    }

    public boolean goBack(){
        backStackMap.get("ABC").goBack();
        return true;
    }

    void setActivity(Activity activity){
        this.activity = activity;
    }

    Activity getActivity(){
        return activity;
    }

    public LinearBackStack getLinearBackstack(String TAG){
        return backStackMap.get(TAG);
    }

    public static class LinearBackStackBuilder{
        ViewCreator creator;
        ViewGroup currentViewGroup;
        ViewGroup container;
        boolean shouldRetain = false;


    }
}
