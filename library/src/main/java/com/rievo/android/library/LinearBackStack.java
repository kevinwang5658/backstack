package com.rievo.android.library;

import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by kwang on 2017-09-14.
 */

public class LinearBackStack implements Reversible{

    static final class State{
        public final String TAG;

        State(String TAG){
            this.TAG = TAG;
        }
    }

    private ViewGroup container;

    //**********************************************************************************************
    // Lifecycle
    //**********************************************************************************************

    LinearBackStack(State state, ViewGroup container){
        this.container = container;
    }

    @Override
    public boolean goBack() {
        return false;
    }
}
