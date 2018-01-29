package com.rievo.backstack.Async;

import android.content.Context;
import android.support.v4.view.AsyncLayoutInflater;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rievo.backstack.R;
import com.rievo.library.AsyncEmitter;
import com.rievo.library.AsyncViewCreator;
import com.rievo.library.BackStack;
import com.rievo.library.Emitter;
import com.rievo.library.LBStack;
import com.rievo.library.LinearBackStack;
import com.rievo.library.Node;
import com.rievo.library.ViewCreator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup4 extends RelativeLayout {

    @BindView(R.id.lbs_vg4_random_num) TextView randomNumView;

    LBStack linearBackStack;

    int num;

    public ViewGroup4(Context context, int num) {
        super(context);
        this.num = num;

        LayoutInflater.from(context).inflate(R.layout.a_viewgroup4, this, true);


        Timber.d("hi");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);

        Timber.d("onAttach");
        linearBackStack = BackStack.getStack(AsyncBackStackActivity.TAG);
        randomNumView.setText(num + "");
    }

    @OnClick(R.id.lbs_vg4_next_screen) public void onClick(){
        linearBackStack.add(Node.builder().viewCreator((layoutInflater, container) -> {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.lbs_viewgroup5, container, false);
            return viewGroup;
        }).build());
    }

    /**
     * Node can be passed in as long as it doesn't reference the view group in any way
     */
    public static class ViewGroup4Creator implements AsyncViewCreator {

        int num;

        ViewGroup4Creator(int num){
            this.num = num;
        }

        @Override
        public void create(AsyncLayoutInflater layoutInflater, ViewGroup container, AsyncEmitter e) {
            e.done(new ViewGroup4(container.getContext(), num));
        }
    }
}
