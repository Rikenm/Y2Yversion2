package com.rikenmaharjan.y2yc.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.fragments.FeedBackSubmitFragment;
import com.rikenmaharjan.y2yc.fragments.HomeFragment;
import com.rikenmaharjan.y2yc.fragments.StoryFragment;
import com.rikenmaharjan.y2yc.fragments.ViewActionFragment;
import com.rikenmaharjan.y2yc.fragments.ViewLotteryResultFragment;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import java.util.HashMap;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private ConstraintLayout constraintLayout;
    private FeedBackSubmitFragment fbsf;
    private ViewLotteryResultFragment vlrf;
    private StoryFragment sf;
    private FragmentManager fm;
    private HomeFragment hm;
    private TextView navUsername;
    public String name;
    public String sender;
    public SessionManager session;
    public ViewActionFragment ac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        name = user.get(SessionManager.KEY_NAME);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();

            }
        });

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        navUsername.setText("Hello, "+ name);


        fbsf = new FeedBackSubmitFragment();
        vlrf = new ViewLotteryResultFragment();
        sf = new StoryFragment();
        hm = new HomeFragment();
        ac = new ViewActionFragment();


        fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction ();
        ft.add(R.id.constraintLayout, hm, "tag1");
        ft.addToBackStack ("myFrag1");
        ft.commit ();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // This will replace the fragment to the view lottery result fragment
        if (id == R.id.nav_lotteryResult) {
            if (vlrf == null)
                vlrf = new ViewLotteryResultFragment();
            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, vlrf);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();
        }

        // This will replace the fragment to the send feedback fragment
        else if (id == R.id.nav_sendFeedback) {
            if (fbsf == null)
                fbsf = new FeedBackSubmitFragment();

            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, fbsf);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();
        }

        // This will replace the fragment to the user info page
        else if (id == R.id.nav_stayInfo) {
            if (sf == null)
                sf = new StoryFragment();
            hideKeyboard(this);
            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, sf);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();
        }

        // This will log out the user and jump to the login page
        else if (id == R.id.nav_manage) {
            session.logoutUser();
        }

        // This will render the action item page
        else if (id == R.id.nav_action) {
            if (ac == null)
                ac = new ViewActionFragment();
            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, ac);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onResume() {
        super.onResume();
    }
}
