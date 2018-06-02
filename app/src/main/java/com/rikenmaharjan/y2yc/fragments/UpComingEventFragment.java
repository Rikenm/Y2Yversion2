package com.rikenmaharjan.y2yc.fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.activities.RecyclerViewAdapter;
import com.rikenmaharjan.y2yc.utils.Events;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.*;

public class UpComingEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerViewAdapter recyclerViewAdapter;
    int[] myImageList;

    // custom variable
    View v;
    private RecyclerView newRecycleView;
    private List<Events> lstEvents;
    private Context context;
    private ProgressBar spinner;
    private ImageView imgView;


    public SessionManager session;
    String id;
    String name;
    String Jwt_Token = new String();




    private OnFragmentInteractionListener mListener;

    public UpComingEventFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UpComingEventFragment newInstance(String param1, String param2) {
        UpComingEventFragment fragment = new UpComingEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

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

    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myImageList = new int[]{R.drawable.noevent};

        // dummy data for the recycleView
        lstEvents = new ArrayList<>();
    }

    // TODO:- change this to post json body
     public void post() throws JSONException {

        //===================
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://y2y.herokuapp.com/events";


        // find the structure
        JSONObject jsonBody = new JSONObject();

         jsonBody.put("eventId", "00UW0000002eqXFMAY");
         jsonBody.put("flag", "addUser");

        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Toast.makeText(getActivity(), "Feedback Sent!!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                Toast.makeText(getActivity(), "There is a problem, Please check your internet", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


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

            // Change the JSON to list of string and send it out.
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                    Log.i("response",response.toString());
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };


        requestQueue.add(stringRequest);

//===================


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    // TODO: Update here, Change time format
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_up_coming_event, container, false);
        /*YOUR CODE HERE*/

        spinner = (ProgressBar)  v.findViewById(R.id.progressBar);
        newRecycleView = (RecyclerView) v.findViewById(R.id.events_recycleView);
        Context context = container.getContext();
        spinner = (ProgressBar)  v.findViewById(R.id.progressBar);
        imgView = (ImageView) v.findViewById(R.id.img_noevent);



        // here
        lstEvents.clear();

        // MARK:- gets data from the server
        //======================
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        spinner.setVisibility(View.VISIBLE);
        imgView.setVisibility(View.GONE);
        // change the url
        String url ="https://y2y.herokuapp.com/events";

        JsonObjectRequest jsonRequest = new JsonObjectRequest( Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            int size = response.getInt("size");
                            JSONArray jsonArray = response.getJSONArray("records");


                            for (int i = 0 ; i < jsonArray.length();i++){

                                JSONObject event = jsonArray.getJSONObject(i);
                                String eventName = event.getString("eventName");
                                String Location = event.getString("Location");

                                String ID = event.getString("ID");
                                boolean rsvp = event.getBoolean("isRsvp'd");
                                //String description = event.getString("Description");

                                JSONObject startTimeJson = event.getJSONObject("startTime");
                                String startTime = startTimeJson.getString("time");
                                String date = startTimeJson.getString("date");

                                JSONObject endTimeJson = event.getJSONObject("EndTime");
                                String endTime = startTimeJson.getString("time");



                                // change time
                                lstEvents.add(new Events(eventName,Location,startTime+"-"+endTime,ID,"N/A",rsvp,date));
                                // works

                                recyclerViewAdapter.notifyDataSetChanged();
                            }

                            // hide there progress bar
                            spinner.setVisibility(View.GONE);

                            // element in events
                            if (jsonArray.length() < 1){
                                // add image of no Events
                                imgView.setVisibility(View.VISIBLE);
                                imgView.setImageResource(myImageList[0]);

                            }





                        }catch(JSONException e){

                            // hide there progress bar
                            Log.i("Error", String.valueOf(e));
                        }

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", ""+error);
                spinner.setVisibility(View.GONE);

            }
        }){

            // header
            @Override
            public Map <String,String> getHeaders() throws AuthFailureError {
                HashMap <String,String> headers = new HashMap<>();

                String token = Jwt_Token;
                String auth = "bearer "+token;
                headers.put("Content-Type", "application/json");
                headers.remove("Authorization");
                headers.put("Authorization", auth);

                return headers;
            }
        };

        queue.add(jsonRequest);

        Log.i("Record", "Goes in ");

        //======================



        recyclerViewAdapter = new RecyclerViewAdapter(container.getContext(),lstEvents);
        newRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newRecycleView.setAdapter(recyclerViewAdapter);


        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
