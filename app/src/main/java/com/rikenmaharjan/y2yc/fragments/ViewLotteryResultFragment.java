package com.rikenmaharjan.y2yc.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.activities.Main2Activity;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by wangdayuan on 4/7/18.
 */

public class ViewLotteryResultFragment extends Fragment {

    private TextView date1;
    private TextView date2;
    private TextView longTermLottery;
    private TextView eBedLottery;
    public SessionManager session;
    String id;
    String name;


    public ViewLotteryResultFragment(){}

    @Override
    public void onResume() {
        super.onResume();

        session = new SessionManager(getActivity());
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // Get the name of the user
        name = user.get(SessionManager.KEY_NAME);

        // Get logged in users user id
        id = user.get(SessionManager.KEY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_view_lottery_result, container, false);
        date1 = (TextView) view.findViewById(R.id.date1);
        date2 = (TextView) view.findViewById(R.id.date2);
        longTermLottery = (TextView) view.findViewById(R.id.longTermLottery);
        eBedLottery = (TextView) view.findViewById(R.id.eBedLottery);

        // Get the current date from the android system
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        date1.setText(currentDateTimeString);
        date2.setText(currentDateTimeString);

        // Use the Volley package to make the Http get request
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://y2y.herokuapp.com/lottery";

        // Create the StringRequest to pass in to request queue later
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request sucessful", response );
                try{
                    JSONObject apiResult = new JSONObject(response);
                    view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                    // Take out the information form the JSON
                    eBedLottery.setText(apiResult.getString("e-bed"));
                    longTermLottery.setText(apiResult.getString("Long Term"));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.i("request failed", "failed");
                Toast.makeText(getActivity(), "There is a problem, Please check your internet", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
        Log.i("result",queue.toString());

        return view;

    }
}
