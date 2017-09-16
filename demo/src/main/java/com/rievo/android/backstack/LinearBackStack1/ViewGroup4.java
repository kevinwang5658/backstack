package com.rievo.android.backstack.LinearBackStack1;

import android.content.Context;
import android.rievo.com.backstack.R;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rievo.android.library.BackStack;
import com.rievo.android.library.LinearBackStack;
import com.rievo.android.library.ViewCreator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup4 extends RelativeLayout {

    @BindView(R.id.lbs_vg4_random_num) TextView randomNumView;

    LinearBackStack linearBackStack;

    int num;

    public ViewGroup4(Context context, int num) {
        super(context);
        this.num = num;

        LayoutInflater.from(context).inflate(R.layout.lbs_viewgroup4, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
        linearBackStack = (LinearBackStack) BackStack.getStack(LinearBackStackActivity.TAG);
        randomNumView.setText(num + "");
    }

    @OnClick(R.id.lbs_vg4_next_screen) public void onClick(){
        linearBackStack.builder((layoutInflater, container) -> {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.lbs_viewgroup5, container, false);
            container.addView(viewGroup);
            return viewGroup;
        })
                .setRetain(true)
                .build();
    }

    /**
     * State can be passed in as long as it doesn't reference the view group in any way
     */
    public static class ViewGroup4Creator implements ViewCreator{

        int num;

        ViewGroup4Creator(int num){
            this.num = num;
        }

        @Override
        public ViewGroup create(LayoutInflater layoutInflater, ViewGroup container) {
            ViewGroup4 viewGroup4 = new ViewGroup4(layoutInflater.getContext(), num);
            container.addView(viewGroup4);
            return viewGroup4;
        }
    }
}
