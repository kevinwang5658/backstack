package com.rievo.android.backstack;

import android.content.Context;
import android.graphics.Color;
import android.rievo.com.backstack.R;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rievo.android.library.LinearBackStack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin on 2017-04-29.
 */

public class ViewGroup2 extends RelativeLayout {

    @BindView(R.id.my_relative_layout) RelativeLayout relativeLayout;
    @BindView(R.id.next_button) Button nextButton;

    public ViewGroup2(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.demo_layout, this, true);
        ButterKnife.bind(this);

        relativeLayout.setBackgroundColor(Color.WHITE);

        nextButton.setOnClickListener(view -> {
            LinearBackStack.get(MainActivity.TAG)
                    .replaceView(new ViewGroup3.ViewGroup3Creator("This is some text"))
                    .done();
        });

    }
}
