package com.rievo.backstack.Async;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rievo.backstack.R;
import com.rievo.library.BackStack;
import com.rievo.library.LinearBackStack;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup6 extends RelativeLayout {

    LinearBackStack linearBackStack;

    public ViewGroup6(Context context) {
        super(context);
    }

    public ViewGroup6(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup6(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
        linearBackStack = (LinearBackStack) BackStack.getStack(AsyncBackStackActivity.TAG);
    }

    @OnClick(R.id.lbs_vg6_next_screen) public void onNextClicked(){
        linearBackStack.add((layoutInflater, container) -> {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.a_viewgroup7, container, false);
            return viewGroup;
        });
    }
}
