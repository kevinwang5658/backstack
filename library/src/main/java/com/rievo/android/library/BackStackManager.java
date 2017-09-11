package com.rievo.android.library;

import android.app.Activity;
import android.widget.RemoteViews;

import java.util.HashMap;

/**
 * Created by kevin on 2017-04-25.
 */
public class BackStackManager {

    /**
     * The root backstack. This will receive the goBack event first
     */
    private static Reversible rootBackStack;

    /**
     * Static map of Linear BackStacks. This is retained on configuration changes
     */
    private static HashMap<String, LinearBackStack> backStackMap = new HashMap<>();

    /**
     * Static map of Cluster Backstacks. This is retained on configuration changes
     */
    private static HashMap<String, ClusterBackStack> clusterBackStackMap = new HashMap<>();

    private static BackStackManager backStackManager;
    private LifeCycleFragment lifeCycleFragment;

    /**
     * Installs a new BackStack Manager. Please call before setContentView in your main Activity
     * @param activity
     */
    public static void install(Activity activity){
        if (backStackManager == null){
            backStackManager = new BackStackManager(activity);
        }
    }

    /**
     * Gets the installed BackstackManager. Only works if the Backstack Manager is installed
     * @return
     */
    public static BackStackManager getBackStackManager(){
        if (backStackManager == null){
            throw new RuntimeException("Please install backstack manager in onCreate of Activity");
        }

        return backStackManager;
    }

    private BackStackManager(Activity activity){
        lifeCycleFragment = LifeCycleFragment.create(activity);
    }

    /**
     * Gets the current activity. This is designed for single activity designs.
     * @return
     */
    Activity getActivity(){
        return lifeCycleFragment.retrieveActivity();
    }

    static void addBackStack(String TAG, LinearBackStack backStack){

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

        if (clusterBackStackMap.get(TAG) != null){
            throw new RuntimeException("Trying to add an already existing cluster backstack. All tags must be unique");
        }

        clusterBackStackMap.put(TAG, backstack);
    }

    static ClusterBackStack getClusterBackStack(String TAG){
        return clusterBackStackMap.get(TAG);
    }

    /**
     * Sets the root BackStack. Must be called or else this will throw an exception when goBack is called.
     * @param backStack
     */
    public static void setRootBackStack(Reversible backStack){
        rootBackStack = backStack;
    }

    /**
     * Global go back. Please call this in {@link Activity#onBackPressed()}
     * @return Whether or not the back was successful. A false means that you are in the root view.
     */
    public boolean goBack(){
        if (rootBackStack == null){
            throw new RuntimeException("root backstack is not set please use BackStack.setRootBackStack(backstack) somewhere");
        }
        return rootBackStack.goBack();
    }
}