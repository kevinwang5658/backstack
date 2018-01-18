package com.rievo.library;

import android.support.v4.view.AsyncLayoutInflater;
import android.view.ViewGroup;

public interface AsyncViewCreator {
    void create(AsyncLayoutInflater inflater, ViewGroup container, AsyncEmitter<ViewGroup> emitter);
}
