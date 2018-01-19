package com.rievo.library;

import android.content.Context;
import android.support.v4.view.AsyncLayoutInflater;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class AsyncBackStack extends RelativeLayout implements BStack{

    private String TAG;

    private AsyncLayoutInflater layoutInflater;
    private LinearState s;

    private List<Pair<Node, ViewGroup>> viewStack = new ArrayList<>();

    public AsyncBackStack(Context context, String TAG, Node firstNode) {
        super(context);
    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean goBack() {
        return false;
    }

    //*****************************
    // Helper
    //********************

    public LinearState getState(){
        return BackStack.getBackStackManager().getLinearState(TAG);
    }

    //**************

    private List<LinearBackStack.Listener> listenerList = new ArrayList<>();

    public void addListener(LinearBackStack.Listener listener){
        listenerList.add(listener);
    }

    public void onAddEvent(){
        for (LinearBackStack.Listener listener: listenerList){
            listener.onAdd(TAG);
        }
    }

    interface Listener{
        void onAdd(String TAG);
    }
}
