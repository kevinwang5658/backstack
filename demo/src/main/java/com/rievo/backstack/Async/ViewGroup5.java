package com.rievo.backstack.Async;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rievo.backstack.R;
import com.rievo.library.AsyncLinearBackStack;
import com.rievo.library.BackStack;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup5 extends RelativeLayout{

    AsyncLinearBackStack linearBackStack;

    public ViewGroup5(Context context) {
        super(context);
    }

    public ViewGroup5(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
        linearBackStack = (AsyncLinearBackStack) BackStack.getStack(AsyncBackStackActivity.TAG);
    }

    @OnClick(R.id.lbs_vg5_next_screen) public void onClick(){
        //Views that are parents to the default container can also be used as containers
        linearBackStack.add((layoutInflater, container, emitter) -> {
           layoutInflater.inflate(R.layout.a_viewgroup6, container, (v, i, p)->{
               emitter.done((ViewGroup) v);
           });
        });

    }
}
