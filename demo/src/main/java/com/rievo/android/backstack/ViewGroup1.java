package com.rievo.android.backstack;

import android.content.Context;
import android.graphics.Color;
import android.rievo.com.backstack.R;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rievo.android.library.LinearBackStack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by kevin on 2017-04-29.
 */

public class ViewGroup1 extends RelativeLayout {

    @BindView(R.id.my_relative_layout) RelativeLayout layout;
    @BindView(R.id.next_button) Button nextButton;

    public ViewGroup1(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.demo_layout, this, true);
        ButterKnife.bind(this);

        layout.setBackgroundColor(Color.BLUE);

        //This is how you do animations
        //I highly recommend the android animations library
        nextButton.setOnClickListener(view -> {
            LinearBackStack.get(MainActivity.TAG)
                    .replaceView((inflater, container) -> {
                        return new ViewGroup2(inflater.getContext());
                    })
                    .addAnimation((view1, complete) -> {
                        YoYo.with(Techniques.SlideInRight)
                                .duration(500)
                                .onEnd((a)->complete.complete())
                                .playOn(view1);
                    })
                    .removeAnimation((view1, complete) -> {
                        YoYo.with(Techniques.SlideOutRight)
                                .duration(500)
                                .onEnd((a)->complete.complete())
                                .playOn(view1);
                    })
                    .done();
        });
    }
}
