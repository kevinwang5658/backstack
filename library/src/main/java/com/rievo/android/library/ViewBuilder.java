package com.rievo.android.library;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kevin on 2017-04-29.
 */

/**
 * Builds a new screen
 */
public class ViewBuilder {

    LinearBackStack linearBackStack;
    ViewGroup container;
    LinearBackStack.ViewCreator viewCreator;
    LinearBackStack.Animation addAnimation = new LinearBackStack.DefaultAnimation();
    LinearBackStack.Animation removeAnimation = new LinearBackStack.DefaultAnimation();

    boolean isIndependent = false;
    boolean allowDuplicates = true;

    ViewBuilder (LinearBackStack linearBackStack){
        this.linearBackStack = linearBackStack;
    }

    ViewBuilder addView(ViewGroup container, LinearBackStack.ViewCreator creator, boolean isIndependent, boolean allowDuplicates){
        this.container = container;
        this.viewCreator = creator;
        this.isIndependent = isIndependent;
        this.allowDuplicates = allowDuplicates;

        return this;
    }

    /**
     * Add an add animation to this ViewGroup. This will be called when the view is added. Don't forget to call complete!!!!!!!!!
     * @param addAnimation
     * @return
     */
    public ViewBuilder addAnimation(LinearBackStack.Animation addAnimation){
        this.addAnimation = addAnimation;

        return this;
    }

    /**
     * Add an remove animation to this ViewGroup. This will be called when the view is removed. Don't forget to call complete!!!!!!!!!
     * @param removeAnimation
     * @return
     */
    public ViewBuilder removeAnimation(LinearBackStack.Animation removeAnimation){
        this.removeAnimation = removeAnimation;

        return this;
    }

    /**
     * Call this when you actually want to create a view. Don't forget me either!!! or else it won't work
     * @return created viewgroup
     */
    public ViewGroup done(){
        return linearBackStack.addView(container, viewCreator, isIndependent, allowDuplicates, addAnimation, removeAnimation);
    }


}