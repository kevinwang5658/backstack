package com.rievo.library;

import android.view.ViewGroup;

/**
 * Created by kwang on 2017-09-15.
 */

/**
 * Used to add animations to ViewGroups
 */
public interface Animator {
    /**
     * Used to add animations to ViewGroups
     * @param v The viewgroup to be animated
     * @param e Call e.done() on animation complete
     */
    void animate(ViewGroup v, Emitter e);
}
