package com.rievo.android.library;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Stack;

import timber.log.Timber;

import static com.rievo.android.library.Helper.disable;
import static com.rievo.android.library.Helper.enable;

/**
 * Created by kwang on 2017-09-14.
 */

/**
 * A linear backstack flow
 */
public class LinearBackStack implements BStack{

    /**
     * State that is persisted
     */
    static final class State{
        public final String TAG;
        Stack<BackStackNode> nodeStack = new Stack<>();

        State(String TAG, BackStackNode defaultNode){
            this.TAG = TAG;
            nodeStack.add(defaultNode);
        }
    }

    /**
     * State that isn't persisted
     */
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

    /**
     * Normal initialization. This will add all retained ViewGroups as well as the final view on the stack
     */
    void init(){
        for (int i = 0; i < s.nodeStack.size(); i++){
            BackStackNode node = s.nodeStack.get(i);
            if (node.shouldRetain){
                currentView = addView(node);
                addToRetainStack(i, node, currentView);

                if (i < s.nodeStack.size() - 1){
                    disable(currentView);
                }
            }
        }

        if (!s.nodeStack.peek().shouldRetain) {
            currentView = addView(s.nodeStack.peek());
        }
    }

    /**
     * Initialize this Linear BackStack ignoring the first node. Use this method if the calling ViewGroup should
     * also be the first view within the stack. Follows the same initialization as {@link #init()}
     * @param firstViewGroup
     */
    void initWithoutFirst(ViewGroup firstViewGroup){
        currentView = firstViewGroup;
        if (s.nodeStack.get(0).shouldRetain){
            addToRetainStack(0, s.nodeStack.get(0), currentView);

            if (s.nodeStack.size() > 1){
                disable(currentView);
            }
        }

        for (int i = 0; i < s.nodeStack.size(); i++){
            BackStackNode node = s.nodeStack.get(i);
            if (node != s.nodeStack.get(0) && node.shouldRetain){
                currentView = addView(node);
                addToRetainStack(i, node, currentView);

                if (i < s.nodeStack.size() - 1){
                    disable(currentView);
                }
            }
        }

        if (s.nodeStack.size() != 1 && !s.nodeStack.peek().shouldRetain) {
            currentView = addView(s.nodeStack.peek());
        }
    }

    public void destroy(){
        ((ViewGroup) container.getParent()).removeView(container);
    }

    //*************************
    // Helper Methods
    //*************************

    //Shortens the method to add to retain stack. Should be called whenever a retained view is added
    private void addToRetainStack(int i, BackStackNode node, ViewGroup viewGroup){
        retainMap.put(i, new RetainHolder(
                (ViewGroup) viewGroup.getParent(),
                node.viewCreator,
                viewGroup
        ));
    }

    //Helps add a view using a backstack node. It is container aware and will add to the correct
    //container
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

    //Helps remove a view. It is retain aware
    private void removeView(BackStackNode backStackNode, ViewGroup viewGroup){
        if (!backStackNode.shouldRetain){
            ViewGroup parent = (ViewGroup) viewGroup.getParent();
            parent.removeView(viewGroup);
        }
    }

    //Helps remove a view. Not retain Aware
    private void forceRemoveView(BackStackNode backStackNode, ViewGroup viewGroup){
        ViewGroup parent = (ViewGroup) viewGroup.getParent();
        parent.removeView(viewGroup);
    }

    //**************************
    // Public Functions
    //**************************

    /**
     * Adds a new view onto the stack. This will add it to the default container of the LinearBackStack.
     * Views that aren't at the top of the stack will non-clickable
     * @param viewCreator The view creator for the new view
     */
    public void add(ViewCreator viewCreator){
        BackStackNode backStackNode = new BackStackNode(viewCreator);
        final ViewGroup tempView = currentView;
        final BackStackNode tempNode = s.nodeStack.peek();

        disable(tempView);
        s.nodeStack.add(backStackNode);
        currentView = addView(backStackNode);
        removeView(tempNode, tempView);
    }

    /**
     * Adds a new view onto the stack. This will add it to the container specified. Only containers that
     * are lower or equal position in the view hierarchy as the default container are allowed.
     * Views that aren't at the top of the stack will non-clickable.
     * @param viewCreator The view creator for the new view
     * @param container The container to add the view to
     */
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

    //Called by the builder. This is the full option add.
    private void build(final BackStackNode backStackNode){
        final ViewGroup tempView = currentView;
        final BackStackNode tempNode = s.nodeStack.peek();

        disable(tempView);
        s.nodeStack.add(backStackNode);
        currentView = addView(backStackNode);
        if (backStackNode.shouldRetain){
            addToRetainStack(s.nodeStack.size() - 1, backStackNode, currentView);
        }

        if (backStackNode.addAnimator != null){
            backStackNode.addAnimator.animate(currentView, new Emitter() {
                @Override
                public void done() {
                    removeView(tempNode, tempView);
                }
            });
        } else {
            removeView(tempNode, tempView);
        }
    }

    /**
     * Creates a builder to help construct the new ViewGroup. This offers the most options for construction.
     * @param viewCreator The view creator for the new view
     * @return A view builder
     */
    public Builder builder(ViewCreator viewCreator){
        return new Builder(this, viewCreator);
    }

    /**
     * Pretty important method. This is responsible for handling the back events for the entire stack
     * @return true if back was handled
     */
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
                Timber.d(retainMap.size() + "");
                currentView = retainMap.get(s.nodeStack.size() - 1).retainedView;
            }
            enable(currentView);

            if (tempNode.removeAnimator != null) {
                tempView.bringToFront();
                tempNode.removeAnimator.animate(tempView, new Emitter() {
                    @Override
                    public void done() {
                        forceRemoveView(s.nodeStack.peek(), tempView);
                    }
                });
            } else {
                forceRemoveView(s.nodeStack.peek(), tempView);
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the size of the back stack
     * @return size
     */
    public int getSize(){
        return s.nodeStack.size();
    }

    //Holds view information for all retained views
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

    /**
     * A builder for a new view group
     */
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

        /**
         * Sets if the view should be retained. Retained views won't get destroyed when new views are added
         * @param shouldRetain Should the view be retained
         * @return
         */
        public Builder setRetain(boolean shouldRetain){
            this.shouldRetain = shouldRetain;
            return this;
        }

        /**
         * Sets the parent container for the new view. The parent
         * @param container
         * @return
         */
        public Builder setContainer(ViewGroup container){
            if (container.getId() == -1){
                throw new RuntimeException("Parent Container must have id set");
            }

            containerId = container.getId();

            return this;
        }

        /**
         * Sets the add animator. This animator will be called when a view is added. Remember
         * to call emitter.done().
         * @param addAnimator The add animation
         * @return
         */
        public Builder addAnimator(Animator addAnimator){
            this.addAnimator = addAnimator;
            return this;
        }

        /**
         * Sets the remove animator. This animator will be called when back is pressed and the view
         * is removed. Remember to call emitter.done().
         * @param removeAnimator The add animation
         * @return
         */
        public Builder removeAnimator(Animator removeAnimator){
            this.removeAnimator = removeAnimator;
            return this;
        }

        /**
         * Builds the new view. The view will be synchronously added in this method. The view will be
         * added to the top of the stack. Views that aren't at the top of the stack will non-clickable
         */
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
