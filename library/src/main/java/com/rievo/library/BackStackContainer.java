package com.rievo.library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public abstract class BackStackContainer extends RelativeLayout{

    private int id = 0;

    public BackStackContainer(Context context) {
        super(context);
        init();
    }

    public BackStackContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackStackContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        id = (int) (Math.random() * 100000);
        setId(id);
    }

    public int getId(){
        return id;
    }

    public abstract String getTag();
}
