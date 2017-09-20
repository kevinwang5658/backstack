package com.rievo.backstack;

import android.content.Intent;
import android.rievo.com.backstack.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rievo.backstack.LinearBackStack1.LinearBackStackActivity;
import com.rievo.backstack.RealisticDemo.RealisticDemoActivity;
import com.rievo.library.BackStackManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Simple BackStack";

    @BindView(R.id.main_activity_list_view) ListView listView;

    BackStackManager backStackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case (0):startActivity(new Intent(MainActivity.this, LinearBackStackActivity.class)); break;
                    case (1):startActivity(new Intent(MainActivity.this, RealisticDemoActivity.class)); break;
                }
            }
        });

    }
}