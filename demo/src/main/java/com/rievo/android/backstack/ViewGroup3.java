package com.rievo.android.backstack;

import android.content.Context;
import android.graphics.Color;
import android.rievo.com.backstack.R;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rievo.android.library.LinearBackStack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin on 2017-04-29.
 */

public class ViewGroup3 extends RelativeLayout {

    @BindView(R.id.my_relative_layout) RelativeLayout relativeLayout;
    @BindView(R.id.next_button) Button nextButton;
    @BindView(R.id.text_view) TextView textView;

    public ViewGroup3(Context context, String text) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.demo_layout, this, true);
        ButterKnife.bind(this);

        relativeLayout.setBackgroundColor(Color.GRAY);

        nextButton.setOnClickListener(view -> {
            LinearBackStack.get(MainActivity.TAG)
                    .replaceView((inflater, container) -> {
                        return new ViewGroup1(inflater.getContext());
                    }).done();
        });

        textView.setText(text);


    }

    public static class ViewGroup3Creator implements LinearBackStack.ViewCreator{

        String text;

        ViewGroup3Creator(String text){
            this.text = text;
        }

        @Override
        public ViewGroup create(LayoutInflater inflater, ViewGroup container) {
            return new ViewGroup3(inflater.getContext(), text);
        }
    }
}
