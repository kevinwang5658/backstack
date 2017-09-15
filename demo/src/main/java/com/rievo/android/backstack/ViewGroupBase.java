package com.rievo.android.backstack;

import android.content.Context;
import android.rievo.com.backstack.R;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.rievo.android.library.BackStack;
import com.rievo.android.library.LinearBackStack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by kwang on 2017-09-15.
 */

public class ViewGroupBase extends RelativeLayout{

    LinearBackStack linearBackStack;

    public ViewGroupBase(Context context) {
        super(context);
    }

    public ViewGroupBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        linearBackStack = BackStack.getBackStackManager().linearBackStackBuilder("ABC")
                .viewCreator((layoutInflater, container) -> {
                    ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.layout, container, false);
                    container.addView(viewGroup);
                    return viewGroup;
                })
                .useCurrentViewGroup(this)
                .shouldRetain(true)
                .build();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.next_button_) public void buttonClicked(){
        linearBackStack.add((layoutInflater, container) -> {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.demo_layout, container, false);
            container.addView(viewGroup);
            return viewGroup;
        });
    }
}
