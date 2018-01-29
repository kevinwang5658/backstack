package com.rievo.library;

import android.content.Context;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by kwang on 2017-09-14.
 */

/**
 * A linear backstack flow
 */
public class LinearBackStack extends RelativeLayout implements LBStack, LBStackDefaults {

    private String TAG;

    private LayoutInflater layoutInflater;
    private LinearState s;

    private SparseArray<Pair<Node, ViewGroup>> viewStack = new SparseArray<>();

    private Animator defaultAddAnimator;
    private Animator defaultRemoveAnimator;

    //***************************************************
    // Lifecycle
    //*******************************************

    //<editor-fold desc="Lifecycle">

    //******************************
    // Initialization
    //***************************

    public LinearBackStack(Context context, String TAG, Node firstNode) {
        super(context);

        this.TAG = TAG;
        layoutInflater = LayoutInflater.from(context);
        BackStack.getBackStackManager().addStack(TAG, this);

        s = getState();
        if (s == null){
            BackStack.getBackStackManager().addLinearState(TAG);

            s = getState();
            s.nodes.add(firstNode);
            addView(0, firstNode);
            //activity start up, first time
        } else {
            //Readd all of the retained views
            for (int counter = 0; counter < s.nodes.size(); counter++) {
                Node node = s.nodes.get(counter);

                if (counter < s.nodes.size() - 1) {
                    addView(counter, node);
                }
            }

            //Readd the top view of the stack
            addView(Helper.lastIndex(s.nodes), Helper.getLast(s.nodes));
        }
    }

    @Override
    public void destroy() {

    }
    //</editor-fold>

    //*****************************
    // Functional
    //*****************************

    public String getTag(){
        return TAG;
    }

    public boolean add(ViewCreator viewCreator){
        return add(Node.builder().viewCreator(viewCreator).build());
    }

    public boolean add(Node node){
        Timber.d("add");

        s.nodes.add(node);

        final Pair<Node, ViewGroup> prev = Helper.getLast(viewStack);
        final int index = s.nodes.size() - 1;
        Helper.disable(prev.second);

        addView(index, node);
        onAddEvent();

        if (node.addAnimator() != null) {
            node.addAnimator().animate(Helper.getLast(viewStack).second, () -> { });
        } else if (defaultAddAnimator != null){
            defaultAddAnimator.animate(Helper.getLast(viewStack).second, () -> { });
        }

        return true;
    }

    @Override
    public boolean goBack() {
        return goBackImpl(this, s, viewStack, defaultRemoveAnimator);
    }

    @Override
    public boolean goToHome(){
        return goToHomeImpl(this, s, viewStack, defaultRemoveAnimator);
    }

    @Override
    public LBStack setDefaultAddAnimator(Animator animator) {
        defaultAddAnimator = animator;
        return this;
    }

    @Override
    public LBStack setDefaultRemoveAnimator(Animator animator) {
        defaultRemoveAnimator = animator;
        return this;
    }

    //*****************************
    // Helper
    //********************

    private LinearState getState(){
        return BackStack.getBackStackManager().getLinearState(TAG);
    }

    public ViewGroup getCurrentView(){
        return viewStack.get(s.nodes.indexOf(Helper.getLastViewNode(s.nodes).second)).second;
    }

    private ViewGroup addView(int position, Node node){
        ViewGroup vg = node.viewCreator().create(layoutInflater, this);
        viewStack.put(position, new Pair<>(node, vg));
        addView(vg);

        return vg;
    }

    //**************

    private List<Listener> addListenerList = new ArrayList<>();

    @Override
    public void setOnAddListener(Listener listener){
        addListenerList.add(listener);
    }

    private void onAddEvent(){
        for (Listener listener: addListenerList){
            listener.onAdd(TAG);
        }
    }


}
