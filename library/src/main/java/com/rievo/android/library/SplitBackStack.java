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

    private HashMap<Integer, String> stackMap = new HashMap<>();
    private State s;

    SplitBackStack(State s){
        this.s = s;
    }

    public void add(int position, String TAG){
        stackMap.put(position, TAG);
    }

    public void remove(int position){
        BackStackManager manager = BackStack.getBackStackManager();
        manager.destroy(stackMap.get(position));
        stackMap.remove(position);
    }

    public void changePosition(int position){
        s.position = position;
    }

    public int getPosition(){
        return s.position;
    }

    public int getSize(){
        return stackMap.size();
    }

    public String getTAG(int position){
        return stackMap.get(position);
    }

    @Override
    public boolean goBack() {
        BStack bStack = BackStack.getStack(stackMap.get(s.position));
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
