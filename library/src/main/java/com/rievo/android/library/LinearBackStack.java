package com.rievo.android.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by kevin on 2017-04-25.
 */

/**
 * Main BackStack class. Default behaviour for this back stack is one container view with one screen.
 * When a new screen is added using {@link #replaceViewBuilder(ViewCreator)}, the previous view will be replaced so that the container will still
 * have a child count of one. You can use {@link #addIndependentViewBuilder(ViewGroup, ViewCreator)} to add a view that won't get destroyed.
 */
public class LinearBackStack implements Reversible{

    final String TAG;

    private ArrayList<BackStackNode> viewBackStack = new ArrayList<>();

    private WeakReference<ViewGroup> rootViewGroup;

    /**
     * The last created view group that is dependent.
     */
    private WeakReference<ViewGroup> topViewGroup;
    /**
     * A list of all created independent views
     */
    private ArrayList<WeakReference<ViewGroup>> independentViewGroups = new ArrayList<>();

    /**
     * Create a new Lienar BackStack. This backstack will be associated with the TAG and will be retained
     * across configuration changes. The default behaviour for a LienarBackStack is that newly added views
     * replace the previous view inside the container. Use the View Creator to create the view so that they
     * can be recreated later on if needed. Be careful not to capture a reference to the enclosing class or else
     * it <b>WILL</b> memory leak. Just a warning.
     * @param TAG The TAG of this Lienar BackStack. Unique
     * @param container The container the views will be added to
     * @param creator The view creator for the first view
     * @return The created view
     */
    public static LinearBackStack create(String TAG, ViewGroup container, ViewCreator creator){
        return LinearBackStack.create(TAG, container, creator, new DefaultAnimation(), new DefaultAnimation());
    }

    /**
     * Create a new Linear BackStack with animations. This backstack will be associated with the TAG and will be retained
     * across configuration changes. The default behaviour for a LienarBackStack is that newly added views
     * replace the previous view inside the container. Use the View Creator to create the view so that they
     * can be recreated later on if needed. Be careful not to capture a reference to the enclosing class or else
     * it <b>WILL</b> memory leak. Just a warning.
     * @param TAG The TAG of this Lienar BackStack. Unique
     * @param container The container the views will be added to
     * @param creator The view creator for the first view
     * @param addAnimation The add animation. Be sure to call complete when it's done
     * @param removeAnimation The remove animation. Be sure to call complete when it's done
     * @return The created view
     */
    public static LinearBackStack create(String TAG, ViewGroup container, ViewCreator creator, Animation addAnimation, Animation removeAnimation){
        boolean newBackStack = false;

        LinearBackStack backStack = BackStackManager.getBackStack(TAG);
        if (backStack == null){
            newBackStack = true;
            backStack = new LinearBackStack(TAG);
            BackStackManager.addBackStack(TAG, backStack);
        }
        //Backstack always be non null beyond this point

        backStack.rootViewGroup = new WeakReference<ViewGroup>(container);
        ViewGroup newlyCreatedViewGroup = null;

        if (newBackStack){
            newlyCreatedViewGroup = backStack.createView(container, creator);

            int id = container.getId();
            if (id == -1){
                throw new RuntimeException("Parent container must have id set");
            }

            backStack.viewBackStack.add(new BackStackNode(id, creator, false, addAnimation, removeAnimation));

        } else {

            int lastNonIndependentNodeCount = backStack.viewBackStack.size() - 1;
            for (; lastNonIndependentNodeCount >= 0; lastNonIndependentNodeCount--){
                if (!backStack.viewBackStack.get(lastNonIndependentNodeCount).isIndependent){
                    break;
                }
            }

            for (int counter = 0; counter < backStack.viewBackStack.size(); counter++){
                BackStackNode node = backStack.viewBackStack.get(counter);
                if (node.isIndependent){
                    ViewGroup independentViewGroup = backStack.createView((ViewGroup) container.getRootView().findViewById(node.containerId), node.viewCreator);
                    backStack.independentViewGroups.add(new WeakReference<ViewGroup>(independentViewGroup));
                } else if (counter == lastNonIndependentNodeCount){
                    newlyCreatedViewGroup = backStack.createView(container, node.viewCreator);
                }
            }

        }

        backStack.topViewGroup = new WeakReference<ViewGroup>(newlyCreatedViewGroup);
        backStack.topViewGroup.get().setClickable(true);
        return backStack;
    }

    /**
     * Get the LinearBackStack associated with the TAG
     * @param TAG
     * @return LinearBackStack
     */
    public static LinearBackStack get(String TAG){
        return BackStackManager.getBackStack(TAG);
    }

    private LinearBackStack(String TAG){
        this.TAG = TAG;
    }

    //**********************************************************************************************
    // View Operations
    //**********************************************************************************************

    //<editor-fold desc="View Operations">

    /**
     * Add an independent view. This view is not associated with the root container although it could be. This view
     * will not be removed when a new view is added, but onBack will remove it.
     * @param container The container that holds the view
     * @param creator Create the view inside here
     * @return A new view builder
     */
    public ViewBuilder addIndependentViewBuilder(ViewGroup container, ViewCreator creator){
        return new ViewBuilder(this).addView(container, creator, true, false);
    }

    /** Refer to link
     * {@link LinearBackStack#addIndependentViewBuilder(ViewGroup, ViewCreator)}
     *
     * @param container The container that holds the view
     * @param creator Create the view inside here
     * @param allowDuplicate Allow duplicate view group to be added, not specifying equals false
     * @return A new view builder
     */
    public ViewBuilder addIndependentViewBuilder(ViewGroup container, ViewCreator creator, boolean allowDuplicate){
        return new ViewBuilder(this).addView(container, creator, true, allowDuplicate);
    }


    public ViewBuilder replaceViewBuilder(ViewCreator creator){
        return new ViewBuilder(this).addView(getRootViewGroup(), creator, false, true);
    }

    ViewGroup addView(ViewGroup container, ViewCreator creator, boolean isIndependent, boolean allowDuplicates, Animation addAnimation, Animation removeAnimation){

        if(!allowDuplicates && viewBackStack.get(viewBackStack.size() - 1).viewCreator.equals(creator)){
            return viewBackStack.get(viewBackStack.size() - 1).isIndependent ? independentViewGroups.get(independentViewGroups.size() - 1).get() : getCurrentViewGroup();
        }

        ViewGroup viewGroup = createView(container, creator);


        if (!isIndependent) {

            final ViewGroup previousViewGroup = topViewGroup.get();
            addAnimation.animate(viewGroup, new AnimationComplete() {
                @Override
                public void complete() {
                    getRootViewGroup().removeView(previousViewGroup);
                }
            });

            topViewGroup = new WeakReference<ViewGroup>(viewGroup);
        } else {
            //is independent
            independentViewGroups.add(new WeakReference<ViewGroup>(viewGroup));
            addAnimation.animate(viewGroup, new DefaultAnimationComplete());
        }

        int id = container.getId();
        if (id == -1){
            throw new RuntimeException("Container must have id set");
        }

        viewBackStack.add(new BackStackNode(id, creator, isIndependent, addAnimation, removeAnimation));

        return viewGroup;
    }

    /*Same as addView but without adding it to the stack*/
    private ViewGroup createView(ViewGroup container, ViewCreator creator){
        ViewGroup viewGroup = creator.create(BackStackManager.getBackStackManager().getActivity().getLayoutInflater(), container);
        container.addView(viewGroup);

        return viewGroup;
    }

    /**
     * Calls the global go back.
     * @return if it was successful
     */
    public boolean globalGoBack(){
        return BackStackManager.getBackStackManager().goBack();
    }

    /**
     * Local go back for this backstack
     * @return  if it was successful
     */
    @Override
    public boolean goBack(){
        //If current page is a cluster backstack then it can handle the back
        boolean isClusterBackSuccessful = false;
        if (getCurrentViewGroup() instanceof ClusterBackStackImpl) {
            isClusterBackSuccessful = ((ClusterBackStackImpl) getCurrentViewGroup()).getClusterBackStack().goBack();
        }

        if (isClusterBackSuccessful) {
            return true;
        } else {
            //cluster couldn't handle the back event

            //checks if this backstack only has 1 view left
            if (viewBackStack.size() < 2) {
                return false;
            }

            BackStackNode nodeToBeRemoved = viewBackStack.get(viewBackStack.size() - 1);
            BackStackNode nodeToBeAdded = viewBackStack.get(viewBackStack.size() - 2);

            final ViewGroup viewToBeRemoved;
            //remove currentNode
            if (!nodeToBeRemoved.isIndependent) {
                viewToBeRemoved = getCurrentViewGroup();
                viewToBeRemoved.bringToFront();

                nodeToBeRemoved.removeAnimation.animate(viewToBeRemoved, new AnimationComplete() {
                    @Override
                    public void complete() {
                        getRootViewGroup().removeView(viewToBeRemoved);
                    }
                });
            } else {
                viewToBeRemoved = independentViewGroups.get(independentViewGroups.size() - 1).get();
                final ViewGroup container = (ViewGroup) viewToBeRemoved.getParent();

                //animation
                nodeToBeRemoved.removeAnimation.animate(viewToBeRemoved, new AnimationComplete() {
                    @Override
                    public void complete() {
                        container.removeView(viewToBeRemoved);
                        independentViewGroups.remove(independentViewGroups.size() - 1);
                    }
                });
            }

            //add previous view
            if (!nodeToBeAdded.isIndependent && !nodeToBeRemoved.isIndependent) {
                ViewGroup newlyCreatedViewGroup = createView(getRootViewGroup(), nodeToBeAdded.viewCreator);

                viewToBeRemoved.bringToFront();

                topViewGroup = new WeakReference<>(newlyCreatedViewGroup);
            }

            viewBackStack.remove(viewBackStack.size() - 1);

            return true;
        }

    }
    //</editor-fold>

    //**********************************************************************************************
    // Getters and Setters
    //**********************************************************************************************

    /**
     * Get the default container
     * @return Default container
     */
    public ViewGroup getRootViewGroup(){
        return rootViewGroup.get();
    }

    /**
     * Get the top dependent viewgroup
     * @return ViewGroup
     */
    public ViewGroup getCurrentViewGroup(){
        return topViewGroup.get();
    }

    public int getChildCount(){
        return viewBackStack.size();
    }

    /**
     * Create your views inside here. Be careful not to capture or else it will leak.
     */
    public interface ViewCreator{
        ViewGroup create(LayoutInflater inflater, ViewGroup container);
    }

    /**
     * Put your animation inside here. Please call complete when it's done to actually remove or add the view.
     */
    public interface Animation{
        void animate(View view, AnimationComplete complete);
    }

    /**
     * Please don't forget me!!!!
     */
    public interface AnimationComplete{
        void complete();
    }

    //***************************
    // No retrolambda for me :(
    //***********************

    public static class DefaultAnimation implements Animation{

        @Override
        public void animate(View view, LinearBackStack.AnimationComplete complete) {
            complete.complete();
        }
    }

    public static class DefaultAnimationComplete implements AnimationComplete{

        @Override
        public void complete() {

        }
    }

}