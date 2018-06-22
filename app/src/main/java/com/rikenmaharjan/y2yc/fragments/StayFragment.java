package com.rikenmaharjan.y2yc.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.activities.HomeRecyclerAdapter;
import com.rikenmaharjan.y2yc.utils.SessionManager;
import com.rikenmaharjan.y2yc.utils.StayModel;
import com.rikenmaharjan.y2yc.utils.WarningModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.http2.ErrorCode;

/**
 * Created by bikenmaharjan on 6/1/18.
 */

public class StayFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView myRecycleView;
    private List<StayModel> data;
    private List<WarningModel> warnings;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private HomeRecyclerAdapter homeRecyclerAdapter2;
    private ImageView errorImage;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout fragmentLayout;

    private Dialog mainActionDialog;
    private Dialog subActionDialog;


    private String id;
    private String name;
    private String Jwt_Token;
    private ProgressBar spinner;

    public StayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    // TODO: Rename and change types and number of parameters
    public static StayFragment newInstance(String param1, String param2) {
        StayFragment fragment = new StayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // data here
        data = new ArrayList<>();
        warnings = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_stay, container, false);
        myRecycleView = (RecyclerView) v.findViewById(R.id.rc_stay);
        spinner = (ProgressBar)  v.findViewById(R.id.progressBarStay);
        errorImage = (ImageView) v.findViewById(R.id.iv_error);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        fragmentLayout = (LinearLayout) v.findViewById(R.id.ll_stay);

        spinner.setVisibility(View.VISIBLE);

        // Activity and Fragment lifecycle don't sync in FIRST fragment; argo sessionData is loaded here
        getSessionData();

        loadData();
        setColorSchemeReload();

        // data-load when pull-down
        swipeContainer.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        spinner.setVisibility(View.GONE);
                        // load the data
                        loadData();


                    }
                }
        );

        return v;
    }


    // reload from the server
    public void loadData(){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://y2y.herokuapp.com/detailuser/";


        errorImage.setVisibility(View.INVISIBLE);

        data.clear();
        warnings.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request sucessful", response );
                try{

                    JSONObject apiResult = new JSONObject(response);


                    // check for null value
                    data.add(new StayModel("Warning","Nil")); // this is not used

                    data.add(new StayModel("Bed",apiResult.getString("Bed_name")));
                    data.add(new StayModel("Last day of Stay",apiResult.getString("Last_Day_Of_Stay")));
                    data.add(new StayModel("Locker Combination",apiResult.getString("Locker")));
                    data.add(new StayModel("NIT",apiResult.getString("NIT")));

                    // warning
                    int majorWarning = apiResult.getInt("Major_warning");
                    int minorWarning = apiResult.getInt("Minor_warning");
                    int suspensionWarning = apiResult.getInt("Suspension_warning");

                    JSONArray warningDetail = apiResult.getJSONArray("Warnings");

                    int i = 0;
                    for (i = 0; i<warningDetail.length();i++){

                        JSONObject warning = warningDetail.getJSONObject(i);

                        // variables from the warning
                        String warningType = warning.getString("warningType");
                        String suspensionStart = warning.getString("suspensionStartDate");
                        String suspensionEnd = warning.getString("suspensionEndDate");

                        JSONObject warningDate = warning.getJSONObject("warningDate");
                        String date = warningDate.getString("date");
                        String warningDescription = warning.getString("warningDescription");


                        warnings.add(new WarningModel(warningDescription,warningType,date,suspensionStart,suspensionEnd,majorWarning,minorWarning,suspensionWarning));

                    }

                    homeRecyclerAdapter.notifyDataSetChanged();
                    spinner.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    // web error
                    spinner.setVisibility(View.GONE);
                    errorImage.setVisibility(View.VISIBLE);
                    fragmentLayout.setBackgroundColor(Color.WHITE);
                    errorImage.setImageResource(R.drawable.error);
                    swipeContainer.setRefreshing(false);

                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.i("request failed", "failed");

                spinner.setVisibility(View.GONE);
                errorImage.setVisibility(View.VISIBLE);
                fragmentLayout.setBackgroundColor(Color.WHITE);
                errorImage.setImageResource(R.drawable.error);
                swipeContainer.setRefreshing(false);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), "Time-out", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getActivity(), "Request can't be Completed, Please Try Again.", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity(), "Server Error, Please Try Again.", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(), "Network Error, Please Try Again.", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getActivity(), "Parse Error, Please Try Again.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "Error, Please Try Again.", Toast.LENGTH_LONG).show();
                }

            }
        }){

            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                String token = Jwt_Token;
                String auth = "bearer "+ token;
                headers.put("Content-Type", "application/json");
                headers.remove("Authorization");
                headers.put("Authorization", auth);
                return headers;
            }


        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                0,
                0));

        queue.add(stringRequest);
        homeRecyclerAdapter = new HomeRecyclerAdapter(getActivity(),data,warnings);
        myRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecycleView.setAdapter(homeRecyclerAdapter);


    }

    // get session data
    public void getSessionData(){

        SessionManager session = new SessionManager(getActivity());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        name = user.get(SessionManager.KEY_NAME);

        // id
        id = user.get(SessionManager.KEY_ID);


        // token
        Jwt_Token = user.get(SessionManager.JWT_Token);


    }


    // color scheme
    public void setColorSchemeReload(){
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }


}
