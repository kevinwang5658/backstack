package com.rievo.android.library;

import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by kwang on 2017-09-13.
 */

public interface ViewCreator {
    /**
     * Must ensure that the view is added to the container by the end of this method
     * @param layoutInflater
     * @param container
     * @return
     */
    ViewGroup create(LayoutInflater layoutInflater, ViewGroup container);
}
