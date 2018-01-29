package com.rievo.library;

import android.app.Activity;
import android.view.ViewGroup;

import java.util.HashMap;

import timber.log.Timber;

/**
 * Created by kwang on 2017-09-13.
 */

/**
 * This is the heart of the library. The lifetime of this class should already be taken care of. This class
 * is created when the app is first created and ends the app ends (when {@link Activity#isFinishing()} returns true not when the process
 * is destroyed). The lifetime should be managed by BackStack and Retained Fragment. This object should be available
 * as soon as {@link BackStack#install(Activity)} is called and should be available in an activity from anytime then on except
 * for inside onDestroy.
 */
public class BackStackManager {

    private HashMap<String, BottomNavState> bottomNavStateMap = new HashMap<>();
    private HashMap<String, LinearState> linearStateMap = new HashMap<>();
    private HashMap<String, BStack> backStackMap = new HashMap<>();
    private String rootBackStackTAG = "";

    //***************************
    // Life Cycle
    //******************

    BackStackManager() {
        Timber.d("New BackStackManager");
    }

    void onDestroy(){
        backStackMap.clear();
    }

    //********************
    // Functions
    //**************

    public LinearBackStack create(String TAG, ViewGroup root, ViewCreator viewCreator){
        LinearBackStack linearBackStack = new LinearBackStack(root.getContext(), TAG, Node.builder().viewCreator(viewCreator).build());
        root.addView(linearBackStack);
        return linearBackStack;
    }

    public AsyncLinearBackStack createAsync(String TAG, ViewGroup root, Node node){
        AsyncLinearBackStack asyncLinearBackStack = new AsyncLinearBackStack(root.getContext(), TAG, node);
        root.addView(asyncLinearBackStack);
        return asyncLinearBackStack;
    }

    public BottomNavBackStack createBottomNavStack(String TAG, int index){
        return new BottomNavBackStack(TAG, index);
    }

    /**
     * Call within {@link Activity#onBackPressed()}. The backstack manager will be notified and internally
     * handle the back event.
     * @return if the back event was handled
     */
    public boolean goBack(){
        if (backStackMap.get(rootBackStackTAG) != null) {
            //We use the root backstack tag if it exists
            return backStackMap.get(rootBackStackTAG).goBack();
        } else if (backStackMap.size() > 0){
            //If not let's just use the first thing in the map
            return ((LBStack) backStackMap.values().toArray()[0]).goBack();
        }

        return false;
    }

    public boolean add(String TAG, ViewCreator viewCreator){
        if (!backStackMap.containsKey(TAG)){
            throw new RuntimeException("TAG does not exist in backstack");
        }

        return getLBStack(TAG).add(Node.builder().viewCreator(viewCreator).build());
    }

    public boolean add(String TAG, AsyncViewCreator viewCreator){
        if (!backStackMap.containsKey(TAG) || !(backStackMap.get(TAG) instanceof AsyncLinearBackStack)){
            throw new RuntimeException("TAG does not exist in backstack");
        }

        return getLBStack(TAG).add(Node.builder().asyncViewCreator(viewCreator).build());
    }

    //****************
    // Accessors
    //*************

    /**
     * Sets the default backstack. The stack that will receive all goBack calls
     * @param TAG
     */
    public void setDefaultRootBackStack(String TAG){
        rootBackStackTAG = TAG;
    }

    /**
     * Retrieves a bottom stack with the TAG
     * @param TAG
     * @return
     */
    public BottomNavBackStack getBottomNavStack(String TAG){
        if (backStackMap.get(TAG) instanceof BottomNavBackStack){
            return (BottomNavBackStack) backStackMap.get(TAG);
        } else {
            return null;
        }
    }

    /**
     * Retrieves a linear back stack using the TAG
     * @param TAG The backstack TAG or null if it doesn't exist
     */
    public LBStack getLBStack(String TAG){
        Timber.d(backStackMap.get(TAG) + "");
        if (backStackMap.get(TAG) instanceof LBStack) {
            return (LBStack) backStackMap.get(TAG);
        } else {
            return null;
        }
    }

    //*********************
    //
    //****************

    /**
     * Adds a stack to the map to be retained and for easier look ups.
     * @param TAG
     * @param bStack
     */
    void addStack(String TAG, BStack bStack){
        backStackMap.put(TAG, bStack);
    }

    void addLinearState(String TAG){
        linearStateMap.put(TAG, new LinearState());
    }

    LinearState getLinearState(String TAG){
        return linearStateMap.get(TAG);
    }

    void addSplitState(String TAG, BottomNavState bottomNavState){
        bottomNavStateMap.put(TAG, bottomNavState);
    }

    BottomNavState getBottomNavState(String TAG){
        return bottomNavStateMap.get(TAG);
    }
}
