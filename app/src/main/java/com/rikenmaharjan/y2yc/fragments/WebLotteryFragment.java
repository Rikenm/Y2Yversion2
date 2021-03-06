package com.rikenmaharjan.y2yc.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import java.util.HashMap;

/**
 * Created by Riken on 5/28/18.
 */

public class WebLotteryFragment extends Fragment {

    public SessionManager session;
    String id;
    String name;
    String Jwt_Token = new String();

   public WebView webview;


    @Override
    public void onResume() {
        super.onResume();
        getSessiondata();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_weblottery, container, false);
        webview = (WebView) view.findViewById(R.id.web);

        getSessiondata();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://www.tfaforms.com/4638535");



        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });



        return view;
    }

    public  void getSessiondata(){

        session = new SessionManager(getActivity());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // Get logged in user's user name
        name = user.get(SessionManager.KEY_NAME);

        // Get looged in user's user id
        id = user.get(SessionManager.KEY_ID);

        Jwt_Token = user.get(SessionManager.JWT_Token);

    }



}
