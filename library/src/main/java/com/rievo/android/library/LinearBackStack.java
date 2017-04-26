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

public class LinearBackStack implements Reversible{

    public String TAG;

    public ArrayList<BackStackNode> viewBackStack = new ArrayList<>();

    private WeakReference<ViewGroup> rootViewGroup;
    private WeakReference<ViewGroup> topViewGroup;
    private ArrayList<WeakReference<ViewGroup>> independentViewGroups = new ArrayList<>();

    public static ViewGroup create(String TAG, ViewGroup container, ViewCreator creator){
        boolean newBackStack = false;

        LinearBackStack backStack = BackStackManager.getBackStack(TAG);
        if (backStack == null){
            newBackStack = true;
            backStack = new LinearBackStack(TAG);
            BackStackManager.addBackStack(TAG, backStack);
        }
        //Backstack always be non null beyond this point

        backStack.rootViewGroup = new WeakReference<ViewGroup>(container);
        ViewGroup newlyCreatedViewGroup;

        if (newBackStack){
            newlyCreatedViewGroup = backStack.addView(container, creator, false);
        } else {

            //Since independent nodes are still visible even if they aren't the current view they have to be added back in order
            ViewGroup lastCreatedIndependentNode = null;
            for (BackStackNode node: backStack.viewBackStack){
                if (node.isIndependent){
                    lastCreatedIndependentNode = backStack.createView((ViewGroup) container.getRootView().findViewById(node.containerId), node.viewCreator);
                    backStack.independentViewGroups.add(new WeakReference<ViewGroup>(lastCreatedIndependentNode));
                }
            }

            //If current node is independent then it has to be added elsewhere and not the root container
            BackStackNode currentNode = backStack.viewBackStack.get(backStack.viewBackStack.size() - 1);
            if (!currentNode.isIndependent){
                newlyCreatedViewGroup = backStack.createView(container, currentNode.viewCreator);
            } else {

                //Adds the last non independent node as well because it can show through
                for (int counter = backStack.viewBackStack.size() - 1; counter >= 0; counter--){
                    if (!backStack.viewBackStack.get(counter).isIndependent){
                        backStack.createView(container, backStack.viewBackStack.get(counter).viewCreator);
                        break;
                    }
                }

                newlyCreatedViewGroup = lastCreatedIndependentNode;
            }

        }

        backStack.topViewGroup = new WeakReference<ViewGroup>(newlyCreatedViewGroup);
        return newlyCreatedViewGroup;
    }

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
    public ViewGroup addIndependentView(ViewGroup container, ViewCreator creator){
        ViewGroup viewGroup = addView(container, creator, true);
        independentViewGroups.add(new WeakReference<ViewGroup>(viewGroup));
        return viewGroup;
    }

    public ViewGroup replaceView(ViewCreator creator){
        if (getRootViewGroup().getChildCount() != 0){
            getRootViewGroup().removeAllViews();
        }

        return addView(getRootViewGroup(), creator, false);
    }

    private ViewGroup addView(ViewGroup container, ViewCreator creator, boolean isIndependent){
        ViewGroup viewGroup = createView(container, creator);
        topViewGroup = new WeakReference<ViewGroup>(viewGroup);
        viewBackStack.add(new BackStackNode(container.getId(), creator, isIndependent));

        return viewGroup;
    }

    /*Same as addView but without adding it to the stack*/
    public ViewGroup createView(ViewGroup container, ViewCreator creator){
        ViewGroup viewGroup = creator.create(BackStackManager.getBackStackManager().getActivity().getLayoutInflater(), container);
        container.addView(viewGroup);

        return viewGroup;
    }

    public boolean globalGoBack(){
        return BackStackManager.getBackStackManager().goBack();
    }

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

            //checks if this backstack is inside of a cluster
            if (viewBackStack.size() < 2) {
                return false;
            }

            BackStackNode nodeToBeRemoved = viewBackStack.get(viewBackStack.size() - 1);
            BackStackNode nodeToBeAdded = viewBackStack.get(viewBackStack.size() - 2);
            ViewGroup newlyCreatedViewGroup;

            //add previous views
            if (!nodeToBeAdded.isIndependent) {
                newlyCreatedViewGroup = createView(getRootViewGroup(), nodeToBeAdded.viewCreator);
            } else {
                newlyCreatedViewGroup = independentViewGroups.get(independentViewGroups.size() - 1).get();
            }

            //remove currentNode
            if (!nodeToBeRemoved.isIndependent) {
                getRootViewGroup().removeView(getCurrentViewGroup());
            } else {
                ViewGroup container = (ViewGroup) getCurrentViewGroup().getRootView().findViewById(nodeToBeRemoved.containerId);
                container.removeView(getCurrentViewGroup());
                independentViewGroups.remove(independentViewGroups.size() - 1);
            }

            topViewGroup = new WeakReference<ViewGroup>(newlyCreatedViewGroup);
            viewBackStack.remove(viewBackStack.size() - 1);

            return true;
        }

    }
    //</editor-fold>

    //**********************************************************************************************
    // Getters and Setters
    //**********************************************************************************************

    public ViewGroup getRootViewGroup(){
        return rootViewGroup.get();
    }

    public ViewGroup getCurrentViewGroup(){
        return topViewGroup.get();
    }

    public interface ViewCreator{
        ViewGroup create(LayoutInflater inflater, ViewGroup container);
    }
}
