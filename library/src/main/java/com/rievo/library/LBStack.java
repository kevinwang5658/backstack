package com.rievo.library;

import android.util.Pair;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

import timber.log.Timber;

import static android.view.View.GONE;

public interface LBStack extends BStack {
    boolean add(Node node);
    boolean add(ViewCreator viewCreator);
    boolean goToHome();
    LBStack setDefaultAddAnimator(Animator animator);
    LBStack setDefaultRemoveAnimator(Animator animator);
    ViewGroup getCurrentView();
    void bringToFront();
    void setOnAddListener(Listener listener);

    interface Listener{
        void onAdd(String TAG);
    }
}
