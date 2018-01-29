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
    @Nullable abstract Action forward();
    @Nullable abstract Action backward();
    @Nullable abstract ViewCreator viewCreator();
    @Nullable abstract Animator addAnimator();
    @Nullable abstract Animator removeAnimator();
    @Nullable abstract AsyncViewCreator asyncViewCreator();

    public static Node create(ViewCreator viewCreator, boolean shouldRetain, Animator addAnimator, Animator removeAnimator, AsyncViewCreator asyncViewCreator) {
        return builder()
                .viewCreator(viewCreator)
                .addAnimator(addAnimator)
                .removeAnimator(removeAnimator)
                .asyncViewCreator(asyncViewCreator)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Node.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder forward(Action forward);
        public abstract Builder backward(Action backward);
        public abstract Builder viewCreator(ViewCreator viewCreator);
        public abstract Builder addAnimator(Animator addAnimator);
        public abstract Builder removeAnimator(Animator removeAnimator);
        public abstract Builder asyncViewCreator(AsyncViewCreator asyncViewCreator);

        abstract Action forward();
        abstract Action backward();
        abstract AsyncViewCreator asyncViewCreator();
        abstract ViewCreator viewCreator();
        abstract Node autoBuild();

        public Node build(){
            if (asyncViewCreator() == null && viewCreator() == null && forward() == null){
                throw new RuntimeException("Both async view creator and view creator " +
                        "and forwards and backwards are not set");
            }

            if (!((forward() == null && backward() == null)
                    || (forward() != null && backward() != null))){
                throw new RuntimeException("Forwards and backwards must be set together");
            }

            return autoBuild();
        }
    }
}
