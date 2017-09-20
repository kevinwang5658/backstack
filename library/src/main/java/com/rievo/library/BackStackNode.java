package com.rievo.library;

/**
 * Created by kwang on 2017-09-14.
 */

/**
 * View Group creation information. Safe for persistence
 */
public class BackStackNode {
    ViewCreator viewCreator;
    int containerId = -1;
    boolean shouldRetain = false;
    Animator addAnimator;
    Animator removeAnimator;


    BackStackNode(ViewCreator viewCreator){
        this.viewCreator = viewCreator;
    }

    BackStackNode(ViewCreator viewCreator, int containerId){
        this(viewCreator);
        this.containerId = containerId;
    }

    BackStackNode(ViewCreator viewCreator, int containerId, boolean shouldRetain){
        this(viewCreator, containerId);
        this.shouldRetain = shouldRetain;
    }

    BackStackNode(ViewCreator viewCreator, int containerId, boolean shouldRetain, Animator addAnimator, Animator removeAnimator){
        this(viewCreator, containerId, shouldRetain);
        this.addAnimator = addAnimator;
        this.removeAnimator = removeAnimator;
    }
}
