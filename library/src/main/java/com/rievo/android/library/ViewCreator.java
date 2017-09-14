package com.rievo.android.library;

import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by kwang on 2017-09-13.
 */

public interface ViewCreator {
    ViewGroup create(LayoutInflater layoutInflater, ViewGroup container);
}
