package com.rievo.library;

import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by kwang on 2017-09-13.
 */

/**
 * Creates a new view group
 */
public interface ViewCreator {
    /**
     * Must ensure that the view is added to the container by the end of this method. Make sure there
     * are no captures to outside references or else this will leak
     * @param layoutInflater a layout inflater
     * @param container parent container
     * @return the newly created View Group
     */
    ViewGroup create(LayoutInflater layoutInflater, ViewGroup container);
}
