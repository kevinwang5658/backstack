package com.rievo.backstack.LinearBackStack1;

import android.content.Context;
import android.util.AttributeSet;

import com.rievo.library.BackStackContainer;

public class BackStackBackStack extends BackStackContainer {

    public BackStackBackStack(Context context) {
        super(context);
    }

    public BackStackBackStack(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackStackBackStack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public String getTag() {
        return null;
    }
}
