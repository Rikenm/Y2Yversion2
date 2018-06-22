package com.rikenmaharjan.y2yc.activities;

import android.app.Activity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.fragments.ActionFragment;
import com.rikenmaharjan.y2yc.fragments.FeedBackSubmitFragment;
import com.rikenmaharjan.y2yc.fragments.HandBookFragment;
import com.rikenmaharjan.y2yc.fragments.HomeFragment;
import com.rikenmaharjan.y2yc.fragments.StayFragment;
import com.rikenmaharjan.y2yc.fragments.StoryFragment;
import com.rikenmaharjan.y2yc.fragments.UpComingEventFragment;
import com.rikenmaharjan.y2yc.fragments.ViewActionFragment;
import com.rikenmaharjan.y2yc.fragments.ViewLotteryResultFragment;
import com.rikenmaharjan.y2yc.fragments.WebLotteryFragment;
import com.rikenmaharjan.y2yc.utils.SessionManager;
import com.rikenmaharjan.y2yc.utils.Survey;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RatingDialogListener {

    private DrawerLayout drawer;
    private ConstraintLayout constraintLayout;
    private FeedBackSubmitFragment fbsf;
    private ViewLotteryResultFragment vlrf;
    private StoryFragment sf;
    private FragmentManager fm;
    private HomeFragment hm;
    private TextView navUsername;
    public String name,id;
    public String sender;
    public SessionManager session;
    public ActionFragment af;
    public ViewActionFragment ac;
    public UpComingEventFragment up;
    public HandBookFragment hb;
    public WebLotteryFragment wf;
    public StayFragment stf;
    public String Jwt_token;
    private ProgressBar pb_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);


        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        name = user.get(SessionManager.KEY_NAME);

        Jwt_token = user.get(SessionManager.JWT_Token);


        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        up = new UpComingEventFragment();
        hb = new HandBookFragment();
        wf = new WebLotteryFragment();
        stf = new StayFragment();
        af = new ActionFragment();


        fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction ();
        ft.add(R.id.constraintLayout, stf, "tag1");
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
            if (stf == null)
                stf = new StayFragment();
            hideKeyboard(this);
            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, stf);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();
        }

        // This will log out the user and jump to the login page
        else if (id == R.id.nav_manage) {
            session.logoutUser();
        }

        // This will render the action item page
        else if (id == R.id.nav_action) {
            if (af == null)
                af = new ActionFragment();
            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, af);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();
        }

        // add new fragment

        else if (id == R.id.nav_event) {
            if (up == null)
                up = new UpComingEventFragment();
            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, up);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();
        }


        else if (id == R.id.nav_handbook) {
            if (hb == null)
                hb = new HandBookFragment();
            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, hb);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();
        }

        else if (id == R.id.nav_weblottery) {
            if (wf == null)
                wf = new WebLotteryFragment();
            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, wf);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();
        }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onResume() {
        super.onResume();

        Log.e("mainactivity","sdsds");

        //SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = this.getSharedPreferences("AndroidHivePref",Context.MODE_PRIVATE);

        SessionManager session = new SessionManager(this);

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        name = user.get(SessionManager.KEY_NAME);

        // id
        id = user.get(SessionManager.KEY_ID);


        // token
        Jwt_token = user.get(SessionManager.JWT_Token);
//



        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());
        if (sharedPref.getString("LAST_LAUNCH_DATE","nodate").contains(currentDate)){
            // Date matches. User has already Launched the app once today. So do nothing.
            Log.i("same date", currentDate);
        }
        else
        {
            // Display dialog text here......
            // Do all other actions for first time launch in the day...
            Log.i("resume started 1st time", currentDate);
            showDialog();

            // Set the last Launched date to today.
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("LAST_LAUNCH_DATE", currentDate);
            editor.commit();
        }




    }


    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                //.setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setNumberOfStars(6)

                .setDefaultRating(3)
                .setTitle("How are you feeling?")

                .setStarColor(R.color.starColor)
                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor)
                .setTitleTextColor(R.color.titleTextColor)
                .setDescriptionTextColor(R.color.contentTextColor)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.hintTextColor)
                .setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.commentBackgroundColor)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .create(Main2Activity.this)
                .show();
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {

        Log.i("Jwt", Jwt_token);

        Survey survey = new Survey(i,s,Jwt_token);
        survey.submitSurvey(this);


    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }
}
