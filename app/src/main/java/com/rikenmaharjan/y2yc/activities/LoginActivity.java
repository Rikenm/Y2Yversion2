package com.rikenmaharjan.y2yc.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.fragments.LoginFragment;
import com.rikenmaharjan.y2yc.fragments.ResetFragment;

public class LoginActivity extends BaseFragmentActivity {

    //BaseFragmentActivity calls the following method and create LoginFragment to add to this activity

    Button btnNewpassword;
    private FragmentManager fm;
    ResetFragment rf;

    FragmentManager fragmentManager = getFragmentManager();

    @Override
    Fragment createFragment() {
        return LoginFragment.newInstance();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}