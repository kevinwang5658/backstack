package com.rievo.library;

import android.arch.lifecycle.ViewModel;

import timber.log.Timber;

public class RetainedViewModel extends ViewModel {
    private BackStackManager backStackManager;

    public void setBackStackManager(BackStackManager backStackManager) {
        this.backStackManager = backStackManager;
    }

    BackStackManager getBackStackManager(){
        return backStackManager;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.d("hi");
    }
}
