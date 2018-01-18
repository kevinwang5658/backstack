package com.rievo.backstack.LinearBackStack1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.rievo.backstack.R;
import com.rievo.library.BackStack;
import com.rievo.library.LinearBackStack;
import com.rievo.library.Node;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup3 extends RelativeLayout {

    @BindView(R.id.lbs_vg3_next_screen) Button button;

    LinearBackStack linearBackStack;
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
        linearBackStack = (LinearBackStack) BackStack.getStack(LinearBackStackActivity.TAG);
        ButterKnife.bind(this);

        button.setText("Pass " + randomNum + " to the next view");

    }

    @OnClick(R.id.lbs_vg3_next_screen) public void onClick(){
        linearBackStack.add(new ViewGroup4.ViewGroup4Creator(randomNum));
    }
}
