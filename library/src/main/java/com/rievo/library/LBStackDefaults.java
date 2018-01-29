package com.rievo.library;

import android.util.Pair;
import android.util.SparseArray;
import android.view.ViewGroup;

import timber.log.Timber;

import static android.view.View.GONE;

interface LBStackDefaults {
    default boolean goBackImpl(ViewGroup vg, LinearState s, SparseArray<Pair<Node, ViewGroup>> viewStack, Animator defaultRemoveAnimator){
        if (s.nodes.size() <= 1){
            Timber.d("last node");
            return false;
        }

        //Apply backwards before checking for views
        if (Helper.getLast(s.nodes).backward() != null){
            Node node = Helper.pop(s.nodes);
            Timber.d("hi");
            node.backward().action(vg);
            return true;
        }

        Timber.d("hi");

        if (Helper.getLast(viewStack).second instanceof Reversible &&
                ((Reversible) Helper.getLast(viewStack).second).onBack()){
            return true;
        }

        Timber.d(viewStack + "");
        //TODO: handle better
        if (s.nodes.size() - 1 > Helper.lastIndex(viewStack)){
            Timber.d(Helper.lastIndex(viewStack) + " " + s.nodes.size());
            return true;
        }

        //pops view from stack
        final Pair<Node, ViewGroup> pair = Helper.pop(viewStack);
        final ViewGroup currentView = pair.second;
        final Node currentNode = Helper.pop(s.nodes);

        Timber.d(pair.first.equals(currentNode) + "");

        Helper.enable(Helper.getLast(viewStack).second);

        //Remove the view. If the animator isn't null then use that first
        if (currentNode.removeAnimator() != null) {
            currentView.bringToFront();
            currentNode.removeAnimator().animate(currentView, new Emitter() {
                @Override
                public void done() {
                    Helper.removeViewAnimation(vg, currentView);
                }
            });
        } else if (defaultRemoveAnimator != null) {
            currentView.bringToFront();
            defaultRemoveAnimator.animate(currentView, new Emitter() {
                @Override
                public void done() {
                    Helper.removeViewAnimation(vg, currentView);
                }
            });
        } else {
            Timber.d("remove");
            vg.removeView(currentView);
        }

        return true;
    }

    default boolean goToHomeImpl(ViewGroup vg, LinearState s, SparseArray<Pair<Node, ViewGroup>> viewStack, Animator defaultRemoveAnimator){
        if (s.nodes.size() <= 1){
            Timber.d("last node");
            return false;
        }

        Helper.enable(viewStack.get(0).second);

        Pair<Integer, Node> pair = Helper.getLastViewNode(s.nodes);
        Node currentNode = pair.second;
        ViewGroup currentView = viewStack.get(pair.first).second;
        int currentIndex = s.nodes.indexOf(pair.second);

        for (int i = viewStack.size() - 1; i > 0; i-- ){
            if (i != pair.first) {
                Pair<Node, ViewGroup> p = viewStack.get(i);

                if (p != null) {
                    vg.removeView(p.second);
                    viewStack.remove(i);
                }
            }
        }

        //Remove the view. If the animator isn't null then use that first
        if (currentNode.removeAnimator() != null) {
            currentView.bringToFront();
            currentNode.removeAnimator().animate(currentView, new Emitter() {
                @Override
                public void done() {
                    Helper.removeViewAnimation(vg, currentView);
                }
            });
        } else if (defaultRemoveAnimator != null) {
            currentView.bringToFront();
            defaultRemoveAnimator.animate(currentView, new Emitter() {
                @Override
                public void done() {
                    Helper.removeViewAnimation(vg, currentView);
                }
            });
        } else {
            vg.removeView(currentView);
        }

        viewStack.remove(currentIndex);

        for (int i = s.nodes.size() - 1; i > 0; i--){
            Helper.pop(s.nodes);
        }

        return true;
    }
}
