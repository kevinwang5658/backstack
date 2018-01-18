package com.rievo.backstack.RealisticDemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rievo.backstack.R;

/**
 * Created by kevin on 2017-09-16.
 */

public class ViewGroup1 extends RelativeLayout{
    
    public ViewGroup1(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.rd_viewgroup1, this, true);
    }
}
