package com.rievo.library;

/**
 * Created by kwang on 2017-09-14.
 */

import android.support.v4.view.AsyncLayoutInflater;

import com.google.auto.value.AutoValue;

import org.jetbrains.annotations.Nullable;

/**
 * View Group creation information. Safe for persistence
 */
@AutoValue
public abstract class Node {
    abstract ViewCreator viewCreator();
    abstract boolean shouldRetain();
    @Nullable abstract Animator addAnimator();
    @Nullable abstract Animator removeAnimator();
    @Nullable abstract AsyncViewCreator asyncViewCreator();

    public static Node create(ViewCreator viewCreator, boolean shouldRetain, Animator addAnimator, Animator removeAnimator, AsyncViewCreator asyncViewCreator) {
        return builder()
                .viewCreator(viewCreator)
                .shouldRetain(shouldRetain)
                .addAnimator(addAnimator)
                .removeAnimator(removeAnimator)
                .asyncViewCreator(asyncViewCreator)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Node.Builder()
                .shouldRetain(false);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder viewCreator(ViewCreator viewCreator);
        public abstract Builder shouldRetain(boolean shouldRetain);
        public abstract Builder addAnimator(Animator addAnimator);
        public abstract Builder removeAnimator(Animator removeAnimator);
        public abstract Builder asyncViewCreator(AsyncViewCreator asyncViewCreator);

        public abstract Node build();
    }
}
