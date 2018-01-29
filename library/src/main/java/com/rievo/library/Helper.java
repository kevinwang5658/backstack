package com.rievo.library;

import android.graphics.Color;
import android.os.Build;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.LinkedHashMap;
import java.util.List;

import timber.log.BuildConfig;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by kwang on 2017-09-15.
 */

public class Helper {

    public static void removeViewAnimation(ViewGroup container, ViewGroup removeView){
        //solves weird animation glitch by delaying removal

        removeView.setVisibility(GONE);

        //pops view from stack and removes view from parent
        container.postDelayed(new Runnable() {
            @Override
            public void run() {
                container.removeView(removeView);
            }
        }, 500);
    }

    public static final String DISABLE = "BACKSTACK_Disable";

    /**
     * Helper function to disable a ViewGroup and all it's children. This draws a new view with z-ordering of integer max
     * that consumes all touch events.
     * @param viewGroup
     */
    public static void disable(ViewGroup viewGroup){
        View view = new View(viewGroup.getContext());
        viewGroup.addView(view);
        view.setTag(DISABLE);

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = MATCH_PARENT;
        params.width = MATCH_PARENT;
        view.setLayoutParams(params);

        view.setClickable(true);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setBackgroundColor(Color.TRANSPARENT);

        if (Build.VERSION.SDK_INT >= 21) {
            view.setTranslationZ(Integer.MAX_VALUE);
        }
        viewGroup.bringChildToFront(view);
    }

    /**
     * Re-enables a view. This will remove the view added
     * @param viewGroup
     */
    public static void enable(ViewGroup viewGroup){
        View view = viewGroup.findViewWithTag(DISABLE);
        if (view != null){
            viewGroup.removeView(view);
        }

    }

    /**
     * Returns last element in a list
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T getLast(List<T> list){
        if (list == null || list.size() == 0){
            return null;
        }

        return list.get(list.size() - 1);
    }

    public static Pair<Integer, Node> getLastViewNode(List<Node> nodeList){
        for (int counter = nodeList.size() - 1; counter >= 0; counter--){
            Node node = nodeList.get(counter);

            if (node.viewCreator() != null || node.asyncViewCreator() != null){
                return new Pair<>(counter, node);
            }
        }

        return new Pair<>(-1, null);
    }

    public static <T> T pop(List<T> list){
        if (list == null || list.size() == 0){
            return null;
        }

        return list.remove(list.size() - 1);
    }

    public static <T> int lastIndex(List<T> list){
        return list.size() - 1;
    }

    /**
     * Returns last element in a list
     * @param sparse
     * @param <T>
     * @return
     */
    public static <T> T getLast(SparseArray<T> sparse){
        if (sparse == null || sparse.size() == 0){
            return null;
        }

        return sparse.get(sparse.keyAt(sparse.size() - 1));
    }

    public static <T> int lastIndex(SparseArray<T> sparse){
        return sparse.keyAt(sparse.size() - 1);
    }

    public static <T> T pop(SparseArray<T> sparse){
        if (sparse == null || sparse.size() == 0){
            return null;
        }

        T returnVal = sparse.get(Helper.lastIndex(sparse));
        sparse.remove(Helper.lastIndex(sparse));
        return returnVal;
    }
}
