package com.rievo.android.library;

/**
 * Created by kevin on 2017-04-25.
 */

public class BackStackNode {
    final LinearBackStack.ViewCreator viewCreator;
    final int containerId;
    final boolean isIndependent;

    BackStackNode(int containerId, LinearBackStack.ViewCreator viewCreator, boolean isIndependent){
        this.viewCreator = viewCreator;
        this.containerId = containerId;
        this.isIndependent = isIndependent;
    }
}
