package com.rievo.android.library;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import timber.log.BuildConfig;
import timber.log.Timber;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by kwang on 2017-09-15.
 */

public class Helper {

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
}
