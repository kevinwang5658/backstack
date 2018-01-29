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
    private BottomNavState s;

    BottomNavBackStack(String TAG, int defaultIndex){
        this.TAG = TAG;
        BackStack.getBackStackManager().addStack(TAG, this);

        s = BackStack.getBackStackManager().getBottomNavState(TAG);
        if (s == null){
            s = new BottomNavState();
            s.index = defaultIndex;

            BackStack.getBackStackManager().addSplitState(TAG, s);
        }
    }

    public BottomNavBackStack attachBottomBar(final BottomNavigationView bottomNavigationView, List<LBStack> linearBackStacks){
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

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                int index = bottomNavigationMap.get(item.getItemId());

                for (int counter = s.list.size() - 1; counter >= 0; counter--){
                    if (s.list.get(counter).first == index && s.list.get(counter).second == Operation.ADD_PAGE){
                        s.list.remove(counter);
                    }
                }

                BackStack.getStack(linearBackStackMap.get(index)).goToHome();
            }
        });

        for (int counter = 0; counter < size; counter++){
            final int index = counter;
            Timber.d(linearBackStackMap.get(counter) + "");
            BackStack.getStack(linearBackStackMap.get(counter)).setOnAddListener(new LinearBackStack.Listener() {
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

    public boolean goUp(){
        return goUp(linearBackStackMap.get(s.index));
    }

    public boolean goUp(String TAG){
        int index = -1;
        for (Map.Entry<Integer, String> entry: linearBackStackMap.entrySet()){
            if (entry.getValue().equals(TAG)){
                index = entry.getKey();
                break;
            }
        }

        if (index == -1){
            throw new RuntimeException("Backstack is not part of bottomNavBackStack");
        }

        for (int counter = s.list.size() - 1; counter >= 0; counter--){
            Pair<Integer, Operation> pair = s.list.get(counter);
            if (pair.first == index && pair.second == Operation.ADD_PAGE){
                s.list.remove(counter);
                break;
            }
        }

        Timber.d(index + "");

        return BackStack.getStack(linearBackStackMap.get(index)).goBack();
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getTag() {
        return TAG;
    }
}
