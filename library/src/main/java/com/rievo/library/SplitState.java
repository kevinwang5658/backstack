package com.rievo.library;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class SplitState {
    public List<Pair<Integer, BottomNavBackStack.Operation>> list = new ArrayList<>();
    public int index = 0;
}
