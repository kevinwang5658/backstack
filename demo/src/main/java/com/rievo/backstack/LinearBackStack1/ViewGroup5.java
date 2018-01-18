package com.rievo.backstack.LinearBackStack1;

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

public class ViewGroup5 extends RelativeLayout{

    LinearBackStack linearBackStack;

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
        linearBackStack = (LinearBackStack) BackStack.getStack(LinearBackStackActivity.TAG);
    }

    @OnClick(R.id.lbs_vg5_next_screen) public void onClick(){
        //Views that are parents to the default container can also be used as containers
        linearBackStack.add((layoutInflater, container) -> {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.lbs_viewgroup6, container, false);
            return viewGroup;
        });
    }
}
