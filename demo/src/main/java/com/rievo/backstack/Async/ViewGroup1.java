package com.rievo.backstack.Async;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rievo.backstack.R;
import com.rievo.library.BackStack;
import com.rievo.library.BackStackManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup1 extends RelativeLayout {

    BackStackManager backStackManager;

    public ViewGroup1(Context context) {
        super(context);
    }

    public ViewGroup1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
        backStackManager = BackStack.getBackStackManager();
    }

    @OnClick(R.id.lbs_vg1_next_screen) public void onNextScreenClicked(){
        //Adding a new screen is as easy as this
        Timber.d("hi");
        backStackManager.add(AsyncBackStackActivity.TAG, (layoutInflater, container, emitter) -> {
            layoutInflater.inflate(R.layout.a_viewgroup2, container, (v, i, p)->{
                emitter.done((ViewGroup) v);
            });
        });
    }
}
