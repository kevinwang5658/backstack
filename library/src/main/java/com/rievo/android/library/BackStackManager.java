package com.rievo.android.library;

import android.app.Activity;

import java.util.HashMap;

/**
 * Created by kevin on 2017-04-25.
 */
public class BackStackManager {

    //I'm assuming that the first added backstack is the root backstack;
    private static Reversible rootBackStack;
    private static HashMap<String, LinearBackStack> backStackMap = new HashMap<>();
    private static HashMap<String, ClusterBackStack> clusterBackStackMap = new HashMap<>();

    private static BackStackManager backStackManager;
    private LifeCycleFragment lifeCycleFragment;

    public static void install(Activity activity){
        if (backStackManager == null){
            backStackManager = new BackStackManager(activity);
        }
    }

    public static BackStackManager getBackStackManager(){
        if (backStackManager == null){
            throw new RuntimeException("Please install backstack manager in onCreate of Activity");
        }

        return backStackManager;
    }

    private BackStackManager(Activity activity){
        lifeCycleFragment = LifeCycleFragment.create(activity);
    }

    Activity getActivity(){
        return lifeCycleFragment.retrieveActivity();
    }

    static void addBackStack(String TAG, LinearBackStack backStack){

        //Assuming that the rootbackstack is the first one added
        if (backStackMap.size() == 0 && clusterBackStackMap.size() == 0){
            rootBackStack = backStack;
        }

        if (backStackMap.get(TAG) != null){
            throw new RuntimeException("Trying to add an already existing backstack. All tags must be unique");
        }

        backStackMap.put(TAG, backStack);
    }

    static LinearBackStack getBackStack(String TAG){
        return backStackMap.get(TAG);
    }

    static int backStackSize(){
        return backStackMap.size();
    }

    static void addClusterBackStack(String TAG, ClusterBackStack backstack){
        if (backStackMap.size() == 0 && clusterBackStackMap.size() == 0){
            rootBackStack = backstack;
        }

        if (clusterBackStackMap.get(TAG) != null){
            throw new RuntimeException("Trying to add an already existing cluster backstack. All tags must be unique");
        }

        clusterBackStackMap.put(TAG, backstack);
    }

    static ClusterBackStack getClusterBackStack(String TAG){
        return clusterBackStackMap.get(TAG);
    }

    public static void setRootBackStack(LinearBackStack backStack){
        rootBackStack = backStack;
    }

    public boolean goBack(){
        return rootBackStack.goBack();
    }
}
