package com.rikenmaharjan.y2yc.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.Map;

/**
 * Created by wangdayuan on 4/7/18.
 */

public class ViewLotteryResultFragment extends Fragment {

    private TextView date1;
    private TextView date2;
    private TextView longTermLottery;
    private TextView eBedLottery;
    public SessionManager session;
    private ProgressBar pbLoading;
    private Button btnReferesh;
    String id;
    String name;
    String Jwt_Token;


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


        //
        Jwt_Token = user.get(SessionManager.JWT_Token);
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
        pbLoading = (ProgressBar) view.findViewById(R.id.loadingPanel);
        btnReferesh =(Button) view.findViewById(R.id.btnReferesh);


        getSessiondata();

        // Get the current date from the android systemdate2.setText(currentDateTimeString);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        btnReferesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbLoading.setVisibility(View.VISIBLE);
                loadData();

            }
        });


        loadData();
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

    public void loadData(){


        /* YOUR CODE HERE*/
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
                    pbLoading.setVisibility(View.GONE);

                    String date = apiResult.getString("e-bed date");
                    String dateLong =apiResult.getString("Long term date");

                    /////
                    if ((apiResult.getString("e-bed")).equals("N/A")){

                        eBedLottery.setText("No Winners");

                    }else {
                        // Take out the information form the JSON
                        eBedLottery.setText(apiResult.getString("e-bed"));
                    }


                    if ((apiResult.getString("Long Term")).equals("N/A")){
                        longTermLottery.setText("No Winners");
                    }
                    else{
                        longTermLottery.setText(apiResult.getString("Long Term"));
                    }

                    if(!date.equals("N/A")){
                        date1.setText(date);
                    }
                    else{
                        date1.setText("No Lottery time");
                    }


                    if(!dateLong.equals("N/A")){
                        date2.setText(dateLong);
                    }
                    else{
                        date2.setText("No Lottery time");
                    }


                    //////

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.i("request failed", "failed");
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap <String,String> headers = new HashMap<>();

                String token = Jwt_Token;
                String auth = "bearer "+ token;
                headers.put("Content-Type", "application/json");
                headers.remove("Authorization");
                headers.put("Authorization", auth);

                return headers;
            }


        };

        queue.add(stringRequest);
        Log.i("result",queue.toString());
    }



}
