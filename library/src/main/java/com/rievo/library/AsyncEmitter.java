package com.rievo.library;

public interface AsyncEmitter<T> {
    void done(T t);
}
