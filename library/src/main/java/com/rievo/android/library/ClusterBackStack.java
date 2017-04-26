package com.rievo.android.library;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by kevin on 2017-04-25.
 */

public class ClusterBackStack implements Reversible{

    public String TAG;

    private int currentPosition;

    ArrayList<String> tagList = new ArrayList<>();

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

    public ClusterBackStack add(LinearBackStack linearBackStack){
        tagList.add(linearBackStack.TAG);
        return this;
    }

    @Override
    public boolean goBack(){
        return BackStackManager.getBackStack(tagList.get(currentPosition)).goBack();
    }

    public void switchContext(int position){
        if (currentPosition >= tagList.size()){
            throw new RuntimeException("new position is out of bounds");
        }

        currentPosition = position;
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    public int size(){
        return tagList.size();
    }

}
