package com.rievo.backstack.LinearBackStack1;

import android.content.Context;
import android.rievo.com.backstack.R;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rievo.android.library.BackStack;
import com.rievo.android.library.LinearBackStack;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup1 extends RelativeLayout {

    LinearBackStack linearBackStack;

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
        linearBackStack = (LinearBackStack) BackStack.getStack(LinearBackStackActivity.TAG);

    }

    @OnClick(R.id.lbs_vg1_next_screen) public void onNextScreenClicked(){
        //Adding a new screen is as easy as this
        linearBackStack.add((layoutInflater, container) -> {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.lbs_viewgroup2, container ,false);
            container.addView(viewGroup);
            return viewGroup;
        });
    }
}
