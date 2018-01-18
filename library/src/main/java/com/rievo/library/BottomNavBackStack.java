package com.rievo.library;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Pair;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by kevin on 2017-09-15.
 */

public class BottomNavBackStack implements BStack {

    enum Operation{
        TAB_SWITCH,
        ADD_PAGE
    }

    private String TAG;

    Map<Integer, String> linearBackStackMap = new HashMap<>();
    Map<Integer, Integer> bottomNavigationMap = new HashMap<>();

    private BottomNavigationView bottomNavigationView;
    private SplitState s;

    BottomNavBackStack(String TAG, int defaultIndex){
        this.TAG = TAG;
        BackStack.getBackStackManager().addStack(TAG, this);

        s = BackStack.getBackStackManager().getSplitState(TAG);
        if (s == null){
            s = new SplitState();
            s.index = defaultIndex;

            BackStack.getBackStackManager().addSplitState(TAG, s);
        }
    }

    public BottomNavBackStack attachBottomBar(final BottomNavigationView bottomNavigationView, List<LinearBackStack> linearBackStacks){
        if (bottomNavigationView.getMenu().size() != linearBackStacks.size()){
            throw new RuntimeException("Number of backstacks doesn't match number of tabs");
        }

        this.bottomNavigationView = bottomNavigationView;

        int size = bottomNavigationView.getMenu().size();

        for (int counter = 0; counter < size; counter++){
            linearBackStackMap.put(counter, linearBackStacks.get(counter).getTag());
        }

        for (int counter = 0; counter < size; counter++){
            bottomNavigationMap.put(bottomNavigationView.getMenu().getItem(counter).getItemId(), counter);
        }

        bottomNavigationView.getMenu().getItem(s.index).setChecked(true);
        BackStack.getStack(linearBackStackMap.get(s.index)).bringToFront();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int index = bottomNavigationMap.get(item.getItemId());

                if (s.index != index) {

                    Timber.d("hi");

                    s.list.add(new Pair<>(s.index, Operation.TAB_SWITCH));
                    s.index = index;
                    BackStack.getStack(linearBackStackMap.get(index)).bringToFront();
                }

                return true;
            }
        });

        for (int counter = 0; counter < size; counter++){
            final int index = counter;
            BackStack.getStack(linearBackStackMap.get(counter)).addListener(new LinearBackStack.Listener() {
                @Override
                public void onAdd(String TAG) {
                    s.list.add(new Pair<>(index, Operation.ADD_PAGE));
                }
            });
        }

        Timber.d(bottomNavigationView.getMenu().getItem(0).getItemId() + "");

        return this;
    }

    @Override
    public boolean goBack() {
        if (s.list.size() == 0){
            return false;
        }

        Pair<Integer, Operation> operation = Helper.pop(s.list);

        switch (operation.second){
            case ADD_PAGE:
                return BackStack.getStack(linearBackStackMap.get(operation.first)).goBack();
            case TAB_SWITCH:
                Timber.d("hi");
                Timber.d(operation.first + " " + operation.second);

                s.index = operation.first;
                bottomNavigationView.getMenu().getItem(operation.first).setChecked(true);
                BackStack.getStack(linearBackStackMap.get(operation.first)).bringToFront();
                return true;
        }

        return false;
    }

    @Override
    public void destroy() {

    }
}
