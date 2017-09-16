package com.rievo.android.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Timer;

import timber.log.Timber;

/**
 * Created by kevin on 2017-09-15.
 */

public class SplitBackStack implements BStack {

    static class State{
        int position = -1;
        State(int position){
            this.position = position;
        }
    }

    private HashMap<Integer, BStack> stackMap = new HashMap<>();
    private State s;

    SplitBackStack(State s){
        this.s = s;
    }

    public void add(int position, BStack bStack){
        Timber.d("add position: " + position);
        stackMap.put(position, bStack);
    }

    public void remove(int position){
        BackStackManager manager = BackStack.getBackStackManager();
        manager.destroy(manager.findTAG(stackMap.get(position)));
        stackMap.remove(position);
    }

    public void changePosition(int position){
        Timber.d("change position: " + position);
        s.position = position;
    }

    @Override
    public boolean goBack() {
        BStack bStack = stackMap.get(s.position);
        if(bStack != null) {
            return bStack.goBack();
        } else {
            return false;
        }
    }

    @Override
    public void destroy() {
        stackMap = null;
    }
}
