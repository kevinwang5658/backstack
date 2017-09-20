package com.rievo.backstack.RealisticDemo;

import android.os.Bundle;
import android.rievo.com.backstack.R;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.rievo.library.BackStack;
import com.rievo.library.BackStackManager;
import com.rievo.library.SplitBackStack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin on 2017-09-16.
 */

public class RealisticDemoActivity extends AppCompatActivity {

    public static final String TAG = "RealisticDemoActivityBackStack";

    @BindView(R.id.root) RelativeLayout root;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottom_navigation;

    BackStackManager backStackManager;
    SplitBackStack splitBackStack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rd_activity);
        BackStack.install(this);
        backStackManager = BackStack.getBackStackManager();

        ButterKnife.bind(this);

        splitBackStack = backStackManager.createSplitBackStack(TAG, 0);


    }

    @Override
    public void onBackPressed() {
        if (!backStackManager.goBack()) {
            super.onBackPressed();
        }
    }
}
