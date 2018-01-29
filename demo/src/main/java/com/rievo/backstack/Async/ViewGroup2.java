package com.rievo.backstack.Async;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rievo.backstack.R;
import com.rievo.library.BackStack;
import com.rievo.library.LBStack;
import com.rievo.library.LinearBackStack;
import com.rievo.library.Node;
import com.rievo.library.Reversible;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup2 extends RelativeLayout implements Reversible{

    @BindView(R.id.lbs_vg2_next_screen) Button button;

    LBStack linearBackStack;
    int clickCounter = 0;

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
        linearBackStack = BackStack.getStack(AsyncBackStackActivity.TAG);
    }


    @OnClick(R.id.lbs_vg2_next_screen) public void onClick(){
        if (clickCounter++ != 0) {
            linearBackStack.add(Node.builder().asyncViewCreator((layoutInflater, container, emitter) -> {
                layoutInflater.inflate(R.layout.a_viewgroup3, container, (v, i, p) -> {
                    emitter.done((ViewGroup) v);
                });
            }).addAnimator((v, e) -> {
                YoYo.with(Techniques.SlideInRight)
                        .duration(200)
                        .onEnd(a -> e.done())
                        .playOn(v);
            }).removeAnimator((v, e) -> {
                Timber.d("HI");

                YoYo.with(Techniques.SlideOutRight)
                        .duration(200)
                        .onEnd(a -> e.done())
                        .playOn(v);

            }).build());
        } else {
            linearBackStack.add(Node.builder().forward((container)->{
                Timber.d("hi");
                Button button = container.findViewById(R.id.lbs_vg2_next_screen);
                button.animate().translationYBy(100).setDuration(200);
            }).backward(container -> {
                Timber.d("hi");
                Button button = container.findViewById(R.id.lbs_vg2_next_screen);
                button.animate().translationYBy(-100).setDuration(200);
            }).build());

        }

    }

    @Override
    public boolean onBack() {
        clickCounter = 0;
        return false;
    }
}
