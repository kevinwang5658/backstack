package com.rievo.backstack.Async;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rievo.backstack.R;
import com.rievo.library.BackStack;
import com.rievo.library.BackStackManager;
import com.rievo.library.Node;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by kevin on 2017-09-16.
 */

public class AsyncBackStackActivity extends AppCompatActivity {

    public static final String TAG = "AsyncBackStackActivity";

    @BindView(R.id.root) RelativeLayout root;

    BackStackManager backStackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackStack.install(this);
        setContentView(R.layout.a_activity);
        ButterKnife.bind(this);

        backStackManager = BackStack.getBackStackManager();
        backStackManager.createAsync(TAG, root, Node.builder().asyncViewCreator((layoutInflater, container, emitter)->{
            layoutInflater.inflate(R.layout.a_viewgroup1, container, (v, resid, parent)->{
                emitter.done((ViewGroup) v);
            });
        }).build());

    }

    @Override
    public void onBackPressed() {
        if (!backStackManager.goBack()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Timber.d("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Timber.d("onDestroy");
    }
}
