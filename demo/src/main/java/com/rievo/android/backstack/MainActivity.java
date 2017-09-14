package com.rievo.android.backstack;

import android.rievo.com.backstack.R;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rievo.android.library.BackStack;
import com.rievo.android.library.BackStackManager;
import com.rievo.android.library.ViewCreator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Simple BackStack";

    @BindView(R.id.root) RelativeLayout root;
    @BindView(R.id.view_pager) ViewPager viewPager;

    BackStackManager backStackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        BackStack.install(this);

        backStackManager = BackStack.getBackStackManager();
        backStackManager.createLinearBackStack("ABC", root, (layoutInflater, container) -> {
            return null;
        });

    }


    @Override
    public void onBackPressed() {
    }
}