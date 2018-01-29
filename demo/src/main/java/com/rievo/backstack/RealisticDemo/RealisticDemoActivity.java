package com.rievo.backstack.RealisticDemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rievo.backstack.Async.AsyncBackStackActivity;
import com.rievo.backstack.LinearBackStack1.LinearBackStackActivity;
import com.rievo.backstack.R;
import com.rievo.library.BackStack;
import com.rievo.library.BackStackManager;
import com.rievo.library.BottomNavBackStack;
import com.rievo.library.LBStack;
import com.rievo.library.LinearBackStack;
import com.rievo.library.Node;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by kevin on 2017-09-16.
 */

public class RealisticDemoActivity extends AppCompatActivity {

    public static final String TAG = "RealisticDemoActivityBackStack";

    @BindView(R.id.root) RelativeLayout root;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottom_navigation;

    BackStackManager backStackManager;
    BottomNavBackStack bottomNavBackStack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rd_activity);

        Timber.d("hi");


        BackStack.install(this);
        backStackManager = BackStack.getBackStackManager();

        ButterKnife.bind(this);

        ArrayList<LBStack> linearBackStacks = new ArrayList<>();
        linearBackStacks.add(backStackManager.create(LinearBackStackActivity.TAG, root, (layoutInflater, container) -> {
            //Be very careful here. The view creator has to return the view that is created not the parent container.
            //LayoutInflator.inflate() will return the container if attachToRoot is true
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.lbs_viewgroup1, container, false);
            return viewGroup;
        }).setDefaultAddAnimator((v, e) -> {
            Animation animation = AnimationUtils.loadAnimation(
                    this, R.anim.slide_in_right
            );
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    e.done();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            v.startAnimation(animation);
        }).setDefaultRemoveAnimator((v, e)->{
            Animation animation = AnimationUtils.loadAnimation(
                    this, R.anim.slide_out_right
            );
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    e.done();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            v.startAnimation(animation);

        }));



        linearBackStacks.add(backStackManager.createAsync(AsyncBackStackActivity.TAG, root, Node.builder().asyncViewCreator((layoutInflater, container, emitter)->{
            layoutInflater.inflate(R.layout.a_viewgroup1, container, (v, resid, parent)->{
                emitter.done((ViewGroup) v);
            });
        }).build()));

        linearBackStacks.add(backStackManager.create(LinearBackStackActivity.TAG + " 1", root, (layoutInflater, container) ->
                new ViewGroup2(layoutInflater.getContext())
        ));

        BottomNavBackStack bottomNavBackStack = BackStack.getBackStackManager()
                .createBottomNavStack(TAG, 0)
                .attachBottomBar(bottom_navigation, linearBackStacks);
        BackStack.getBackStackManager().setDefaultRootBackStack(TAG);

        Timber.d(R.id.item_1 + "");


    }

    @Override
    public void onBackPressed() {
        if (!backStackManager.goBack()) {
            super.onBackPressed();
        }
    }
}
