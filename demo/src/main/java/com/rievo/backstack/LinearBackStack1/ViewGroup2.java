package com.rievo.backstack.LinearBackStack1;

import android.content.Context;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rievo.backstack.R;
import com.rievo.library.BackStack;
import com.rievo.library.LinearBackStack;
import com.rievo.library.Node;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

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

        Timber.d("onAttach");

        ButterKnife.bind(this);
        linearBackStack = (LinearBackStack) BackStack.getStack(LinearBackStackActivity.TAG);
    }

    @OnClick(R.id.lbs_vg2_next_screen) public void onClick(){
        linearBackStack.add(Node.builder().viewCreator((layoutInflater, container) -> {
            return (ViewGroup) layoutInflater.inflate(R.layout.lbs_viewgroup3, container, false);
        }).addAnimator((v, e) -> {
            YoYo.with(Techniques.SlideInRight)
                    .duration(200)
                    .onEnd(a->e.done())
                    .playOn(v);
        }).removeAnimator((v, e) -> {
            Timber.d("HI");

            YoYo.with(Techniques.SlideOutRight)
                    .duration(200)
                    .onEnd(a->e.done())
                    .playOn(v);

        }).build());
    }

}
