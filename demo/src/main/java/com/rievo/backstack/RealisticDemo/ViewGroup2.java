package com.rievo.backstack.RealisticDemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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

public class ViewGroup2 extends RelativeLayout {

    public ViewGroup2(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.rd_viewgroup2, this, true);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_1) public void onClick(){
        BackStack.getStack(LinearBackStackActivity.TAG + " 1")
                .add(Node.builder().viewCreator((inflater, container)->{
                    return new ViewGroup3(inflater.getContext());
                }).build());
    }

}
