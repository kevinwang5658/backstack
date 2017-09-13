package com.rievo.android.library;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import timber.log.Timber;

/**
 * Created by kevin on 2017-04-25.
 */

/**
 * A cluster back stack is useful for viewpagers and bottom bars. It does not store view creators,
 * it merely delegates back events to the correct LinearBackStack. Use {@link #switchContext(int)}
 * to specify which Linear Backstack should get the back event.
 */
public class ClusterBackStack implements Reversible{

    public String TAG;

    private int currentPosition;

    private SparseArray<String> tagList = new SparseArray<>();

    /**
     * Create a new cluster backstack with the TAG as the identifier. This backstack will be
     * retained across rotations, so your selected position will be saved. A cluster back stack
     * is useful for viewpagers and bottom bars. It does not store view creators, it merely delegates
     * back events to the correct LinearBackStack. Use {@link #switchContext(int)} to specify which
     * Linear Backstack should get the back event.
     * @param TAG The tag of the cluster backstack
     * @param startPosition The starting position
     * @return The created backstack
     */
    public static ClusterBackStack create(String TAG, int startPosition){
        boolean newBackStack = false;

        ClusterBackStack backStack = BackStackManager.getClusterBackStack(TAG);
        if (backStack == null) {
            backStack = new ClusterBackStack(TAG, startPosition);
            BackStackManager.addClusterBackStack(TAG, backStack);
        }
        //Back stack is non null beyond this point

        return backStack;
    }

    private ClusterBackStack(String TAG, int startPosition){
        this.TAG = TAG;
        this.currentPosition = startPosition;
    }

    /**
     * Add a new Linear Backstack. The position of this backstack will be the next available position,
     * Using {@link #add(int, LinearBackStack)} instead is recommended. Only unique linear backstacks
     * can be added
     * @param linearBackStack
     * @return Whether or not the backstack was added successfully
     */
    public boolean add(LinearBackStack linearBackStack){
        if (tagList.indexOfValue(linearBackStack.TAG) < 0) {
            tagList.append(tagList.size(), linearBackStack.TAG);
            return true;
        }
        return false;
    }

    /**
     * Add a new Linear BackStack at a given position. It is recommended this position is the same as
     * the position of the viewgroup inside the viewpager or bottom bar. Only unique linear backstacks
     * are accepted
     * @param position
     * @param linearBackStack
     * @return Whether or not the backstack was added successfully
     */
    public boolean add(int position, LinearBackStack linearBackStack){
        if (tagList.indexOfValue(linearBackStack.TAG) < 0 && tagList.get(position) == null) {
            tagList.append(position, linearBackStack.TAG);
            return true;
        }
        return false;
    }

    /**
     * Local go back for this cluster backstack
     * @return whether or not this this was successful
     */
    @Override
    public boolean goBack(){
        return BackStackManager.getBackStack(tagList.get(currentPosition)).goBack();
    }

    /**
     * Change the selected position of this backstack. This will change which view will get the goBack
     * event.
     * @param position
     */
    public void switchContext(int position){
        if (tagList.get(position) == null){
            throw new RuntimeException("New Cluster Backstack position is out of bounds");
        }

        currentPosition = position;
    }

    /**
     * The current select position
     * @return the current position
     */
    public int getCurrentPosition(){
        return currentPosition;
    }

    /**
     * The size of this cluster backstack.
     * @return the size of the backstack
     */
    public int size(){
        return tagList.size();
    }

}