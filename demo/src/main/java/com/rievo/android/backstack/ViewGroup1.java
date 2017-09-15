package com.rievo.android.backstack;

import android.content.Context;
import android.rievo.com.backstack.R;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rievo.android.library.BackStack;
import com.rievo.android.library.Reversible;

import timber.log.Timber;

/**
 * Created by kwang on 2017-09-14.
 */

public class ViewGroup1 extends RelativeLayout implements Reversible{

    Button button;

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
        init();
    }

    private void init(){
        button = (Button) findViewById(R.id.next_button);
        button.setOnClickListener(view -> {
            BackStack.get("ABC")
                    .builder((layoutInflater, container) -> {
                        //Be very careful this retains the parent ViewGroup container
                        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.leak_canary_heap_dump_toast, container, false);
                        container.addView(viewGroup);
                        return viewGroup;
                    })
                    .setContainer((ViewGroup) getRootView().findViewById(R.id.root))
                    .setRetain(true)
                    .addAnimator((v, e) -> {
                        YoYo.with(Techniques.SlideInDown)
                                .duration(500)
                                .onEnd((animator)->{
                                    e.done();
                                })
                                .playOn(v);
                    })
                    .removeAnimator((v, e) -> {
                        YoYo.with(Techniques.SlideOutDown)
                                .duration(500)
                                .onEnd((animator)->{
                                    e.done();
                                })
                                .playOn(v);
                    })
                    .build();
        });
    }

    @Override
    public boolean onBack() {
        return false;
    }
}
