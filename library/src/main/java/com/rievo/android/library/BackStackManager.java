package com.rievo.android.library;

import android.app.Activity;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

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

    private Activity activity;
    private HashMap<String, BStack> backStackMap = new HashMap<>();
    private HashMap<String, LinearBackStack.State> linearStateMap = new HashMap<>();
    private HashMap<String, SplitBackStack.State> splitStateMap = new HashMap<>();
    private String rootBackStackTAG = "";

    BackStackManager(Activity activity) {
        this.activity = activity;

        Timber.d("New BackStackManager");
    }

    void onDestroy(){
        backStackMap.clear();
        activity = null;
    }

    /**
     * Creates a new linear BackStack. Backstacks will handle all back navigation for it's ViewGroups as along as
     * all ViewGroups were added using the backstacks add or build methods. This BackStack's state will get persisted
     * through rotations. All backstacks must have a unique tag to reference it.
     * @param TAG Unique tag to reference back stack
     * @param container Default container to place views
     * @param firstView View Creator for first view in backstack
     * @return the created backstack
     */
    public LinearBackStack createLinearBackStack(String TAG, ViewGroup container, ViewCreator firstView){
        LinearBackStack.State state = retrieveOrCreateState(TAG, firstView);
        LinearBackStack linearBackStack = new LinearBackStack(state, container, activity);
        backStackMap.put(TAG, linearBackStack);
        linearBackStack.init();
        setRootBackStackTAG(TAG);

        return linearBackStack;
    }

    public SplitBackStack createSplitBackStack(String TAG, int defaultPosition){
        SplitBackStack.State state = splitStateMap.get(TAG);
        if (state == null){
            state = new SplitBackStack.State(defaultPosition);
            splitStateMap.put(TAG, state);
        }
        SplitBackStack splitBackStack = new SplitBackStack(state);
        backStackMap.put(TAG, splitBackStack);
        setRootBackStackTAG(TAG);

        return splitBackStack;
    }

    /**
     * Builds a new linear BackStack. Backstacks will handle all back navigation for it's ViewGroups as along as
     * all ViewGroups were added using the backstacks add or build methods. This BackStack's state will get persisted
     * through rotations. All backstacks must have a unique tag to reference it.
     * @param TAG Unique tag to reference back stack
     * @return the created backstack
     */
    public LinearBackStackBuilder builder(String TAG){
        return new LinearBackStackBuilder(this, TAG);
    }

    //Called by the builder to build the backstack
    private LinearBackStack buildLinearBackStack(String TAG, ViewGroup container, ViewGroup currentView, ViewCreator viewCreator, boolean shouldRetain, boolean allowDuplicates){
        LinearBackStack.State state = linearStateMap.get(TAG);
        if (state == null){
            BackStackNode backStackNode = new BackStackNode(viewCreator, container.getId(), shouldRetain);
            state = new LinearBackStack.State(TAG, backStackNode);
            state.allowDuplicates = allowDuplicates;
            linearStateMap.put(TAG, state);
        }

        LinearBackStack linearBackStack = new LinearBackStack(state, container, activity);
        if (currentView == null){
            linearBackStack.init();
        } else {
            if (state.nodeStack.size() != 1 && !shouldRetain){
                container.removeView(currentView);
            }
            linearBackStack.initWithoutFirst(currentView);
        }
        backStackMap.put(TAG, linearBackStack);
        setRootBackStackTAG(TAG);

        return linearBackStack;
    }

    public void destroy(String TAG){
        backStackMap.get(TAG).destroy();
        backStackMap.remove(TAG);
    }

    //Helper method to retrieve state for linear back stack
    private LinearBackStack.State retrieveOrCreateState(String TAG, ViewCreator firstView){
        LinearBackStack.State state = linearStateMap.get(TAG);
        if (state == null){
            BackStackNode backStackNode = new BackStackNode(firstView);
            state = new LinearBackStack.State(TAG, backStackNode);
            linearStateMap.put(TAG, state);
        }

        return state;
    }

    private void setRootBackStackTAG(String TAG){
        if (rootBackStackTAG.equals("")){
            rootBackStackTAG = TAG;
        }
    }

    /**
     * Call within {@link Activity#onBackPressed()}. The backstack manager will be notified and internally
     * handle the back event.
     * @return if the back event was handled
     */
    public boolean goBack(){
        BStack bStack = backStackMap.get(rootBackStackTAG);
        if (bStack != null) {
            return bStack.goBack();
        } else {
            return false;
        }
    }

    //Sets the activity
    void setActivity(Activity activity){
        this.activity = activity;
    }

    //Gets the activity
    Activity getActivity(){
        return activity;
    }

    /**
     * Sets the default backstack. All {@link #goBack()} calls will be directed to this backstack
     * @param TAG
     */
    public void setRootBackStack(String TAG){
        rootBackStackTAG = TAG;
    }

    /**
     * Retrieves a linear back stack using the TAG
     * @param TAG The backstack TAG or null if it doesn't exist
     */
    public BStack getStack(String TAG){
        return backStackMap.get(TAG);
    }

    public String findTAG(BStack bStack){
        for (Map.Entry<String, BStack> entry: backStackMap.entrySet()){
            if (entry.getValue() == bStack){
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Builds a new linear back stack
     */
    public static class LinearBackStackBuilder{

        BackStackManager backStackManager;

        String TAG;
        ViewCreator creator;
        ViewGroup currentViewGroup;
        ViewGroup container;
        boolean allowDuplicates = true;
        boolean shouldRetain = false;

        private LinearBackStackBuilder(BackStackManager backStackManager, String TAG){
            this.backStackManager = backStackManager;
            this.TAG = TAG;
        }

        /**
         * The ViewCreator for the first view in the stack. This must be set.
         * @param viewCreator the first view creator
         * @return
         */
        public LinearBackStackBuilder viewCreator(ViewCreator viewCreator){
            this.creator = viewCreator;
            return this;
        }

        /**
         * Sets the default container for the linear back stack
         * @param container the parent container
         * @return
         */
        public LinearBackStackBuilder setContainer(ViewGroup container) {
            this.container = container;
            return this;
        }

        /**
         * Use the current ViewGroup as the first View within the stack. {@link #setContainer(ViewGroup)} doesn't have
         * to be set if this is set. If both are set the container must be a parent of currentViewGroup.
         * @param currentViewGroup the view group to be used as first view within the stack
         * @return
         */
        public LinearBackStackBuilder useCurrentViewGroup(ViewGroup currentViewGroup) {
            this.currentViewGroup = currentViewGroup;
            return this;
        }

        /**
         * Should the first view be retained. See {@link LinearBackStack.Builder#setRetain(boolean)}
         * @param shouldRetain should the view be retained
         * @return
         */
        public LinearBackStackBuilder shouldRetain(boolean shouldRetain){
            this.shouldRetain = shouldRetain;
            return this;
        }

        public LinearBackStackBuilder shouldAllowDuplicates(boolean allowDuplicates){
            this.allowDuplicates = allowDuplicates;
            return this;
        }

        /**
         * Creates the new back stack
         * @return
         */
        public LinearBackStack build(){
            if (creator == null){
                throw new RuntimeException("View Creator must be set");
            }

            if (currentViewGroup == null && container == null){
                throw new RuntimeException("Either CurrentView must be set or Parent Container must be set");
            }

            if (currentViewGroup != null && container != null && currentViewGroup.getParent() != container){
                throw new RuntimeException("Container must be the parent of CurrentView");
            }

            if (container == null){
                container = (ViewGroup) currentViewGroup.getParent();
            }

            if (container == null){
                throw new RuntimeException("Container must be set");
            }

            if (container.getId() == -1){
                throw new RuntimeException("Container must have id set");
            }

            return backStackManager.buildLinearBackStack(TAG, container, currentViewGroup, creator, shouldRetain, allowDuplicates);
        }
    }
}
