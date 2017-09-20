package com.rievo.backstack.LinearBackStack1;

import android.os.Bundle;
import android.rievo.com.backstack.R;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rievo.android.library.BackStack;
import com.rievo.android.library.BackStackManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin on 2017-09-16.
 */

public class LinearBackStackActivity extends AppCompatActivity {

    public static final String TAG = "LinearBackStackActivity";

    @BindView(R.id.root) RelativeLayout root;

    BackStackManager backStackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lbs_activity);
        ButterKnife.bind(this);
        BackStack.install(this);

        backStackManager = BackStack.getBackStackManager();
        backStackManager.createLinearBackStack(TAG, root, (layoutInflater, container) -> {
            //Be very careful here. The view creator has to return the view that is created not the parent container.
            //LayoutInflator.inflate() will return the container if attachToRoot is true
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.lbs_viewgroup1, container, false);
            container.addView(viewGroup);
            return viewGroup;
        });

    }

    @Override
    public void onBackPressed() {
        if (!backStackManager.goBack()) {
            super.onBackPressed();
        }
    }
}
