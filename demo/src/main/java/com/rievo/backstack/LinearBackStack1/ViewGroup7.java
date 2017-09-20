package com.rievo.backstack.LinearBackStack1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.rievo.backstack.MainActivity;
import com.rievo.backstack.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup7 extends RelativeLayout {

    public ViewGroup7(Context context) {
        super(context);
    }

    public ViewGroup7(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup7(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.lbs_vg7_done) public void onClick(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        ((Activity) getContext()).startActivity(intent);
    }
}
