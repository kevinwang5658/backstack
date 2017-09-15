package com.rievo.android.backstack;

import android.rievo.com.backstack.R;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.rievo.android.library.BackStack;
import com.rievo.android.library.BackStackManager;
import com.rievo.android.library.ViewCreator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Simple BackStack";

    @BindView(R.id.topReplace) RelativeLayout topReplace;

    BackStackManager backStackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        BackStack.install(this);

        backStackManager = BackStack.getBackStackManager();
        backStackManager.createLinearBackStack("ABC", topReplace, (layoutInflater, container) -> {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.demo_layout, container, false);
            container.addView(viewGroup);
            return viewGroup;
        });
    }


    @Override
    public void onBackPressed() {
        if (!backStackManager.goBack()){
            super.onBackPressed();
        }
    }
}