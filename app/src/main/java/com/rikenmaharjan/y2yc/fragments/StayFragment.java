package com.rikenmaharjan.y2yc.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.activities.HomeRecyclerAdapter;
import com.rikenmaharjan.y2yc.utils.SessionManager;
import com.rikenmaharjan.y2yc.utils.StayModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StayFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private RecyclerView myRecycleView;
    private List<StayModel> data;
    HomeRecyclerAdapter homeRecyclerAdapter;


    private String id;
    private String name;
    private String Jwt_Token;

    public StayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StayFragment.
     */
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://y2y.herokuapp.com/detailuser/";


        data = new ArrayList<>();

        // dummy data
//        data.add(new StayModel("bed","Pod A lower"));
//        data.add(new StayModel("bed1","Pod A lower"));
//        data.add(new StayModel("bed2","Pod A lower"));
//        data.add(new StayModel("bed3","Pod A lower"));
//        data.add(new StayModel("bed4","Pod A lower"));




        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request sucessful", response );
                try{
                    JSONObject apiResult = new JSONObject(response);

//                    view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//                    // get bed assignment
//                    txtBedR.setText(apiResult.getString("Bed_name"));
//                    //get last day of stay
//                    String date = apiResult.getString("Last_Day_Of_Stay");
//                    date = date.replace("-", "");
//                    Date now = new Date();
//                    String currentDateTimeString = DateFormat.getInstance().format(now);
//                    String[] currentDate = currentDateTimeString.split("/");
//                    String month = currentDate[0];
//                    String day = currentDate[1];
//                    if(Integer.parseInt(month) < 10)
//                        month = "0" + month;
//                    if(Integer.parseInt(day)<10){
//                        day = "0" + day;
//                    }
//                    String cdate = "20" + currentDate[2].split(" ")[0]+ month + day;
//                    //compare the date with the current date
//                    if(date.equals("N/A")) {
//                        txtDayR.setText("N/A");
//                    }
//                    else {
//                        if (Integer.parseInt(cdate) > Integer.parseInt(date))
//                            txtDayR.setText("N/A");
//                        else
//                            txtDayR.setText(date);
//                    }
//                    //get locker number
//                    txtLockerR.setText(apiResult.getString("Locker"));
//                    //get number of warnings
//                    txtMinor.setText(apiResult.getString("Minor_warning")+" Minor");
//                    txtMajor.setText(apiResult.getString("Major_warning")+" Major");
//                    //get nit
//                    String nit = apiResult.getString("NIT");
//                    if(nit.equals("0")){
//                        nit = "No";
//                    }
//                    else
//                        nit = "Yes";
//                    txtNITR.setText(nit);

                    Log.i("StayFrag","Volley loop");



                    // check for null value
                    data.add(new StayModel("Bed",apiResult.getString("Bed_name")));
                    data.add(new StayModel("Last day of Stay",apiResult.getString("Last_Day_Of_Stay")));
                    data.add(new StayModel("Locker Combination",apiResult.getString("Locker")));

                    // control nil condition for warnings
                    data.add(new StayModel("Warning","Nil"));
                    data.add(new StayModel("NIT",apiResult.getString("NIT")));

                    homeRecyclerAdapter.notifyDataSetChanged();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.i("request failed", "failed");
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



        queue.add(stringRequest);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_stay, container, false);


        myRecycleView = (RecyclerView) v.findViewById(R.id.rc_stay);
        homeRecyclerAdapter = new HomeRecyclerAdapter(container.getContext(),data);
        myRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecycleView.setAdapter(homeRecyclerAdapter);

        return v;
    }



}
