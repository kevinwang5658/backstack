package com.rievo.android.backstack;

import android.graphics.Color;

import com.rievo.android.library.ClusterBackStack;
import com.rievo.android.library.ClusterBackStackImpl;
import com.rievo.android.library.LinearBackStack;
import com.rievo.android.library.BackStackManager;

import android.rievo.com.backstack.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        BackStackManager.install(this);

    }


    @Override
    public void onBackPressed() {
        BackStackManager.getBackStackManager().goBack();
    }
}