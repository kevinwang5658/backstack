package com.rievo.backstack.Async;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.rievo.backstack.R;
import com.rievo.library.AsyncLinearBackStack;
import com.rievo.library.BackStack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup3 extends RelativeLayout {

    @BindView(R.id.lbs_vg3_next_screen) Button button;

    AsyncLinearBackStack linearBackStack;
    int randomNum = (int) (Math.random() * 1000);

    public ViewGroup3(Context context) {
        super(context);
    }

    public ViewGroup3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        linearBackStack = (AsyncLinearBackStack) BackStack.getStack(AsyncBackStackActivity.TAG);
        ButterKnife.bind(this);

        setBackgroundColor((int) (Math.random() * Integer.MAX_VALUE));

        button.setText("Pass " + randomNum + " to the next view");

    }

    @OnClick(R.id.lbs_vg3_next_screen) public void onClick(){
        linearBackStack.add(new ViewGroup4.ViewGroup4Creator(randomNum));
    }
}
