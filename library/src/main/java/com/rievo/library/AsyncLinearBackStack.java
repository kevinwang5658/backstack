package com.rievo.library;

import android.arch.core.util.Function;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.AsyncLayoutInflater;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class AsyncLinearBackStack extends RelativeLayout implements LBStack, LBStackDefaults{

    private String TAG;

    private LayoutInflater sLayoutInflater;
    private AsyncLayoutInflater layoutInflater;
    private LinearState s;

    private boolean startUpComplete = false;

    private SparseArray<Pair<Node, ViewGroup>> viewStack = new SparseArray<>();

    private Animator defaultAddAnimator;
    private Animator defaultRemoveAnimator;

    public AsyncLinearBackStack(Context context, String TAG, Node firstNode) {
        super(context);

        this.TAG = TAG;
        sLayoutInflater = LayoutInflater.from(context);
        layoutInflater = new AsyncLayoutInflater(getContext());
        BackStack.getBackStackManager().addStack(TAG, this);
        Timber.d(BackStack.getStack(TAG) + "");

        s = getState();
        if (s == null){
            BackStack.getBackStackManager().addLinearState(TAG);

            s = getState();
            s.nodes.add(firstNode);

            addTopViewAsync(s.nodes);
            startUpComplete = true;

            //activity start up, first time
        } else {
            //Readd the top view of the stack
            int index = addTopViewAsync(s.nodes);

            if (s.nodes.size() == 1){
                startUpComplete = true;
            }

            //Readd all of the retained views
            if (s.nodes.size() > 1){
                addAllViewsAsync(s.nodes.subList(0, Helper.lastIndex(s.nodes)), index);
            }

        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public boolean add(ViewCreator viewCreator) {
        if (!startUpComplete){
            return false;
        }

        Helper.disable(Helper.getLast(viewStack).second);
        Node node = Node.builder().viewCreator(viewCreator).build();
        s.nodes.add(node);
        addView(node, Helper.lastIndex(s.nodes));

        return true;
    }

    public boolean add(AsyncViewCreator asyncViewCreator){
        return add(Node.builder().asyncViewCreator(asyncViewCreator).build());
    }

    @Override
    public boolean add(Node node){
        if (!startUpComplete){
            Timber.e("not complete");
            return false;
        }

        //Apply forwards before checking views
        if (node.forward() != null){
            Timber.d("hi");
            s.nodes.add(node);
            node.forward().action(this);
            onAddEvent();
            return true;
        }

        Helper.disable(Helper.getLast(viewStack).second);

        s.nodes.add(node);
        addViewAsync(node, Helper.lastIndex(s.nodes), (v)->{
            if (node.addAnimator() != null) {
                node.addAnimator().animate(v, () -> { });
            } else if (defaultAddAnimator != null){
                defaultAddAnimator.animate(v, () -> { });
            }

            return null;
        });

        return true;
    };

    @Override
    public boolean goBack() {
        if (!startUpComplete){
            return false;
        }

        return goBackImpl(this, s, viewStack, defaultRemoveAnimator);
    }

    @Override
    public boolean goToHome(){
        Timber.d(s.nodes + "");

        if (!startUpComplete){
            return false;
        }

        return goToHomeImpl(this, s, viewStack, defaultRemoveAnimator);
    }

    @Override
    public LBStack setDefaultAddAnimator(Animator animator){
        defaultAddAnimator = animator;
        return this;
    }

    @Override
    public LBStack setDefaultRemoveAnimator(Animator animator){
        defaultRemoveAnimator = animator;
        return this;
    }

    //*****************************
    // Helper
    //********************

    private LinearState getState(){
        return BackStack.getBackStackManager().getLinearState(TAG);
    }

    @Override
    public ViewGroup getCurrentView(){
        return viewStack.get(s.nodes.indexOf(Helper.getLastViewNode(s.nodes).second)).second;
    }

    private void addViewAsync(Node node, int position){
        addViewAsync(node, position, (a)->{
            return null;
        });
    }

    private void addViewAsync(Node node, int position, Function<ViewGroup, Void> listener){
        Timber.d(position + "");
        //Defaults to sychronous add
        if (node.asyncViewCreator() != null) {
            Timber.d("ab");
            node.asyncViewCreator().create(layoutInflater, this, (v)->{
                addView(v);
                viewStack.put(position, new Pair<>(node, v));
                listener.apply(v);
                onAddEvent();
            });
        } else {
            Timber.d("b");
            ViewGroup v = addView(node, position);
            viewStack.put(position, new Pair<>(node, v));
            listener.apply(v);
            onAddEvent();
        }
    }

    private int addTopViewAsync(List<Node> nodeList){
        Pair<Integer, Node> viewPair = Helper.getLastViewNode(s.nodes);

        Node node = viewPair.second;
        int position = viewPair.first;

        Timber.d(position + "");

        if (node.asyncViewCreator() != null) {
            node.asyncViewCreator().create(layoutInflater, this, (v)->{
                addView(v);
                viewStack.put(position, new Pair<>(node, v));

                if (position < nodeList.size() - 1){
                    applyForwardOperations(nodeList, position + 1);
                }
            });
        } else {
            ViewGroup v = addView(node, position);
            viewStack.put(position, new Pair<>(node, v));
            if (position < nodeList.size() - 1){
                applyForwardOperations(nodeList, position + 1);
            }
        }

        return viewPair.first;
    }

    private void applyForwardOperations(List<Node> nodeList, int position){
        for (int counter = position; counter < nodeList.size(); counter++){
            Timber.d("hi " + position);
            nodeList.get(position).forward().action(this);
        }
    }

    private void addAllViewsAsync(List<Node> nodelist, int lastIndex){
        Timber.d(nodelist.size() + "");

        ViewGroup vgs[] = new ViewGroup[lastIndex];
        CountDownLatch countDownLatch = new CountDownLatch(lastIndex, (a)->{
            for (int counter = vgs.length - 1; counter >= 0; counter--){

                Timber.d(counter + "");

                if (vgs[counter] != null) {
                    addView(vgs[counter], 0);
                    viewStack.put(counter, new Pair<>(nodelist.get(counter), vgs[counter]));

                    if (counter < vgs.length - 1 && vgs[counter + 1] == null){
                        for (int counter1 = counter + 1; counter1 < vgs.length; counter1++){
                            Timber.d("hi");

                            if (vgs[counter1] == null){
                                nodelist.get(counter1).forward().action(this);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            startUpComplete = true;

            return null;
        });

        for (int counter = 0; counter < lastIndex; counter++){
            Node node = nodelist.get(counter);
            final int position = counter;

            if (node.asyncViewCreator() != null){
                node.asyncViewCreator().create(layoutInflater, this, (v)->{
                    vgs[position] = v;
                    countDownLatch.decrement();
                });
            } else if (node.viewCreator() != null) {
                vgs[position] = node.viewCreator().create(sLayoutInflater, this);
                countDownLatch.decrement();
            } else {
                countDownLatch.decrement();
            }
        }
    }

    private ViewGroup addView(Node node, int position){
        ViewGroup vg = node.viewCreator().create(sLayoutInflater, this);
        viewStack.put(position, new Pair<>(node, vg));
        addView(vg);

        return vg;
    }

    private static class CountDownLatch {

        private Function<Void, Void> listener;
        private int count;

        CountDownLatch(int counter, Function<Void, Void> listener){
            this.listener = listener;
            count = counter;
        }

        void decrement(){
            if (--count == 0){
                listener.apply(null);
            }
        }
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
