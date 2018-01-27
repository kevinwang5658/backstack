package com.rievo.backstack.RealisticDemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rievo.backstack.LinearBackStack1.LinearBackStackActivity;
import com.rievo.backstack.R;
import com.rievo.library.BackStack;
import com.rievo.library.Node;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup3 extends RelativeLayout {

    public ViewGroup3(Context context) {
        super(context);
        init();
    }

    public ViewGroup3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewGroup3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.rd_viewgroup3, this, true);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_1) public void onClick(){
        BackStack.getBackStackManager().getBottomNavStack(RealisticDemoActivity.TAG).goUp();
    }
}
