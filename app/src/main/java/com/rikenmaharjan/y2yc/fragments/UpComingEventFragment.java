package com.rikenmaharjan.y2yc.fragments;

import android.content.Context;
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



import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.activities.RecyclerViewAdapter;
import com.rikenmaharjan.y2yc.utils.Events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    // custom variable
    View v;
    private RecyclerView newRecycleView;
    private List<Events> lstEvents;
    private Context context;




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


    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // dummy data for the recycleView
        lstEvents = new ArrayList<>();
        lstEvents.add(new Events("Y2Y demo","Boston","12:00-3:00pm"));
        lstEvents.add(new Events("Y2Y demo","Boston","12:00-3:00pm"));


        // MARK:- gets data from the server
        //======================
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // change the url 
        String url ="http://192.168.0.11:3000/events";

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

                                JSONObject friends = jsonArray.getJSONObject(i);
                                String eventName = friends.getString("eventName");
                                String Location = friends.getString("Location");
                                lstEvents.add(new Events(eventName,Location,"12:00-3:00pm"));
                                // works
                                recyclerViewAdapter.notifyDataSetChanged();
                            }




                        }catch(JSONException e){

                            Log.i("Error", String.valueOf(e));
                        }

                    }
                },new Response.ErrorListener() {
                  @Override
                    public void onErrorResponse(VolleyError error) {
                     Log.e("Volley", ""+error);
            }
        }){

            // header
            @Override
            public Map <String,String> getHeaders() throws AuthFailureError {
                HashMap <String,String> headers = new HashMap<>();

                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiMDAzVzAwMDAwMG5nYWNGSUFRIiwibmFtZSI6IlJpa2VuIE1haGFyamFuIn0sImlhdCI6MTUyNzI0ODU0N30.awXGHSUYB8afroEB7hrc4Yh08QuQ1K4--U4UurivXng";
                String auth = "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiMDAzVzAwMDAwMG5nYWNGSUFRIiwibmFtZSI6IlJpa2VuIE1haGFyamFuIn0sImlhdCI6MTUyNzI0ODU0N30.awXGHSUYB8afroEB7hrc4Yh08QuQ1K4--U4UurivXng";
                headers.put("Content-Type", "application/json");
                headers.remove("Authorization");
                headers.put("Authorization", auth);

                return headers;
            }
        };

        queue.add(jsonRequest);

        Log.i("Record", "Goes in ");

        //======================


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    // TODO: Update here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_up_coming_event, container, false);
        /*YOUR CODE HERE*/

        newRecycleView = (RecyclerView) v.findViewById(R.id.events_recycleView);
        Context context = container.getContext();
        // here
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