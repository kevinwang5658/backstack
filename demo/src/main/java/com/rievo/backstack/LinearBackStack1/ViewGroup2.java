package com.rievo.backstack.LinearBackStack1;

import android.content.Context;
import android.rievo.com.backstack.R;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rievo.android.library.BackStack;
import com.rievo.android.library.LinearBackStack;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup2 extends RelativeLayout{

    LinearBackStack linearBackStack;

    public ViewGroup2(Context context) {
        super(context);
    }

    public ViewGroup2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
        linearBackStack = (LinearBackStack) BackStack.getStack(LinearBackStackActivity.TAG);
    }

    @OnClick(R.id.lbs_vg2_next_screen) public void onClick(){
        linearBackStack.builder((layoutInflater, container) -> {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.lbs_viewgroup3, container, false);
            container.addView(viewGroup);
            return viewGroup;
        }).addAnimator((v, e) -> {
            YoYo.with(Techniques.SlideInRight)
                    .duration(200)
                    .onEnd((animator)->e.done())
                    .playOn(v);
        }).removeAnimator((v, e) -> {
            YoYo.with(Techniques.SlideOutRight)
                    .duration(200)
                    .onEnd((animator)->e.done())
                    .playOn(v);
        }).build();
    }

}
