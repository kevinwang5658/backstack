package com.rievo.android.library;
/**
 * Created by kevin on 2017-04-25.
 */

class BackStackNode {
    final LinearBackStack.ViewCreator viewCreator;
    final int containerId;
    final boolean isIndependent;
    final LinearBackStack.Animation addAnimation;
    final LinearBackStack.Animation removeAnimation;

    BackStackNode(int containerId, LinearBackStack.ViewCreator viewCreator, boolean isIndependent, LinearBackStack.Animation addAnimation, LinearBackStack.Animation removeAnimation){
        this.viewCreator = viewCreator;
        this.containerId = containerId;
        this.isIndependent = isIndependent;
        this.addAnimation = addAnimation;
        this.removeAnimation = removeAnimation;
    }
}
