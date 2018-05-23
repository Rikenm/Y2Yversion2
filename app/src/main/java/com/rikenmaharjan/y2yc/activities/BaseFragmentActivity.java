package com.rikenmaharjan.y2yc.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rikenmaharjan.y2yc.R;

/**
 * Created by Riken on 3/18/18.
 */

public abstract class BaseFragmentActivity extends AppCompatActivity {

 abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_base);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.activity_fragment_base_fragmentContainer);

        if(fragment == null){
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.activity_fragment_base_fragmentContainer,fragment).commit();
        }
    }
}
