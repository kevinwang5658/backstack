package com.rievo.android.library;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Stack;

import static com.rievo.android.library.Helper.disable;
import static com.rievo.android.library.Helper.enable;

/**
 * Created by kwang on 2017-09-14.
 */

public class LinearBackStack implements BStack{

    static final class State{
        public final String TAG;
        Stack<BackStackNode> nodeStack = new Stack<>();

        State(String TAG, BackStackNode defaultNode){
            this.TAG = TAG;
            nodeStack.add(defaultNode);
        }
    }

    private ViewGroup container;
    private HashMap<Integer, RetainHolder> retainMap = new HashMap<>();
    private ViewGroup currentView;
    private State s;
    private Activity activity;
    private LayoutInflater layoutInflater;

    //**********************************************************************************************
    // Lifecycle
    //**********************************************************************************************

    LinearBackStack(State state, ViewGroup container, Activity activity){
        this.container = container;
        this.s = state;
        this.activity = activity;
        this.layoutInflater = activity.getLayoutInflater();
    }

    void init(){
        for (int i = 0; i < s.nodeStack.size(); i++){
            BackStackNode node = s.nodeStack.get(i);
            if (node.shouldRetain){
                currentView = addView(node);
                addToRetainStack(i, node, currentView);
            }
        }

        if (!s.nodeStack.peek().shouldRetain) {
            currentView = addView(s.nodeStack.peek());
        }
    }

    void initWithoutFirst(ViewGroup firstViewGroup){
        currentView = firstViewGroup;
        if (s.nodeStack.get(0).shouldRetain){
            addToRetainStack(0, s.nodeStack.get(0), currentView);
        }

        for (int i = 0; i < s.nodeStack.size(); i++){
            BackStackNode node = s.nodeStack.get(i);
            if (node != s.nodeStack.get(0) && node.shouldRetain){
                currentView = addView(node);
                addToRetainStack(i, node, currentView);
            }
        }

        if (s.nodeStack.size() != 1 && !s.nodeStack.peek().shouldRetain) {
            currentView = addView(s.nodeStack.peek());
        }
    }

    private void addToRetainStack(int i, BackStackNode node, ViewGroup viewGroup){
        retainMap.put(i, new RetainHolder(
                (ViewGroup) viewGroup.getParent(),
                node.viewCreator,
                viewGroup
        ));
    }

    private ViewGroup addView(BackStackNode backStackNode){
        //Gets the correct parent
        ViewGroup parent = null;
        if (backStackNode.containerId != -1){
            parent = container.getRootView().findViewById(backStackNode.containerId);
        } else {
            parent = container;
        }

        return backStackNode.viewCreator.create(layoutInflater, parent);

    }

    private void removeView(BackStackNode backStackNode, ViewGroup viewGroup){
        if (!backStackNode.shouldRetain){
            ViewGroup parent = (ViewGroup) viewGroup.getParent();
            parent.removeView(viewGroup);
        }
    }

    private void forceRemoveView(BackStackNode backStackNode, ViewGroup viewGroup){
        ViewGroup parent = (ViewGroup) viewGroup.getParent();
        parent.removeView(viewGroup);
    }

    public void add(ViewCreator viewCreator){
        BackStackNode backStackNode = new BackStackNode(viewCreator);
        final ViewGroup tempView = currentView;
        final BackStackNode tempNode = s.nodeStack.peek();

        disable(tempView);
        s.nodeStack.add(backStackNode);
        currentView = addView(backStackNode);
        removeView(tempNode, tempView);
    }

    public void add(ViewCreator viewCreator, ViewGroup container){
        if (container.getId() == -1){
            throw new RuntimeException("Container must have an id set");
        }
        BackStackNode backStackNode = new BackStackNode(viewCreator, container.getId());

        final ViewGroup tempView = currentView;
        final BackStackNode tempNode = s.nodeStack.peek();

        disable(tempView);
        s.nodeStack.add(backStackNode);
        currentView = addView(backStackNode);
        removeView(tempNode, tempView);
    }

    public void build(final BackStackNode backStackNode){
        final ViewGroup tempView = currentView;
        final BackStackNode tempNode = s.nodeStack.peek();

        disable(tempView);
        s.nodeStack.add(backStackNode);
        currentView = addView(backStackNode);
        if (backStackNode.shouldRetain){
            addToRetainStack(retainMap.size(), backStackNode, currentView);
        }

        if (backStackNode.addAnimator != null){
            backStackNode.addAnimator.animate(currentView, ()->{
                removeView(tempNode, tempView);
            });
        } else {
            removeView(tempNode, tempView);
        }
    }

    public Builder builder(ViewCreator viewCreator){
        return new Builder(this, viewCreator);
    }

    @Override
    public boolean goBack() {
        if (currentView instanceof Reversible && ((Reversible) currentView).onBack()){
            return true;
        }

        if (s.nodeStack.size() > 1) {
            final ViewGroup tempView = currentView;
            final BackStackNode tempNode = s.nodeStack.pop();
            final BackStackNode backStackNode = s.nodeStack.peek();

            if (tempNode.shouldRetain){
                retainMap.remove(s.nodeStack.size());
            }

            if (!backStackNode.shouldRetain) {
                currentView = addView(backStackNode);
            } else {
                currentView = retainMap.get(s.nodeStack.size() - 1).retainedView;
            }
            enable(currentView);

            if (tempNode.removeAnimator != null) {
                tempNode.removeAnimator.animate(tempView, ()->{
                    forceRemoveView(s.nodeStack.peek(), tempView);
                });
            } else {
                forceRemoveView(s.nodeStack.peek(), tempView);
            }

            return true;
        } else {
            return false;
        }
    }

    static class RetainHolder {
        ViewGroup container;
        ViewCreator viewCreator;
        ViewGroup retainedView;

        RetainHolder(ViewGroup container, ViewCreator viewCreator, ViewGroup independent){
            this.container = container;
            this.viewCreator = viewCreator;
            this.retainedView = independent;
        }
    }

    public static class Builder{

        LinearBackStack linearBackStack;
        private ViewCreator viewCreator;
        private boolean shouldRetain = false;
        private int containerId = -1;
        private Animator addAnimator;
        private Animator removeAnimator;


        private Builder(LinearBackStack linearBackStack, ViewCreator viewCreator){
            this.linearBackStack = linearBackStack;
            this.viewCreator = viewCreator;
        }

        public Builder setRetain(boolean shouldRetain){
            this.shouldRetain = shouldRetain;
            return this;
        }

        public Builder setContainer(ViewGroup container){
            if (container.getId() == -1){
                throw new RuntimeException("Parent Container must have id set");
            }

            containerId = container.getId();

            return this;
        }

        public Builder addAnimator(Animator addAnimator){
            this.addAnimator = addAnimator;
            return this;
        }

        public Builder removeAnimator(Animator removeAnimator){
            this.removeAnimator = removeAnimator;
            return this;
        }

        public void build(){
            BackStackNode backStackNode = new BackStackNode(
                    viewCreator,
                    containerId,
                    shouldRetain,
                    addAnimator,
                    removeAnimator
            );

            linearBackStack.build(backStackNode);
        }
    }


}
