package com.rikenmaharjan.y2yc.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.android.volley.Header;
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
import com.rikenmaharjan.y2yc.activities.ActionRecyclerAdapter;
import com.rikenmaharjan.y2yc.utils.ActionModel;
import com.rikenmaharjan.y2yc.utils.SessionManager;
import com.rikenmaharjan.y2yc.utils.SubAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bikenmaharjan on 6/3/18.
 */

public class ActionFragment extends BaseFragment {


    private RecyclerView aRecycleView ;
    private ActionRecyclerAdapter actionRecyclerAdapter;
    private String id;
    private String name;
    private String Jwt_Token;
    private ImageView iv_Error;
    private ArrayList<ActionModel> data;
    private ProgressBar pb;
    private LinearLayout ll_action;
    private SwipeRefreshLayout swipereContainer;


    public static List<String> action_item_ids = new ArrayList<>();
    public static List<String[]> action_item_step_ids = new ArrayList<>();
    public static List<Integer> action_item_num_steps = new ArrayList<>();

    public ActionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getSessionData();
    }

    public static ActionFragment newInstance(String param1, String param2) {
        ActionFragment fragment = new ActionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // initial the data here
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new ArrayList<ActionModel>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;

        v = inflater.inflate(R.layout.fragment_action, container, false);

        aRecycleView = (RecyclerView) v.findViewById(R.id.rv_action);
        pb = (ProgressBar) v.findViewById(R.id.pb_action);
        ll_action = (LinearLayout) v.findViewById(R.id.ll_action);
        swipereContainer = (SwipeRefreshLayout) v.findViewById(R.id.swiperefreshAction);
        ll_action.setBackgroundColor(Color.parseColor("#f7f7f7"));
        iv_Error = (ImageView) v.findViewById(R.id.iv_action_error);

        pb.setVisibility(View.VISIBLE);


        loadData();


        swipereContainer.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        pb.setVisibility(View.GONE);
                        loadData();


                    }
                }
        );



        return v;
    }

    public void loadData(){

        String url = "https://y2y.herokuapp.com/actionitems/";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        iv_Error.setVisibility(View.INVISIBLE);
        data.clear();
        ll_action.setBackgroundColor(Color.parseColor("#f7f7f7"));

        if (actionRecyclerAdapter != null) {
            actionRecyclerAdapter.notifyDataSetChanged();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request successful", response );

                try{
                    HashMap<String, List<String>> Child = new HashMap<String, List<String>>();
                    ArrayList<String> Header = new ArrayList<String>();

                    JSONObject apiResult = new JSONObject(response);

                    int num_action_items;
                    int num_steps;
                    List<String[]> childList = new ArrayList<>();
                    num_action_items = Integer.parseInt(apiResult.getString("size"));

                    if (num_action_items == 0) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "There are currently no action items planned.", Toast.LENGTH_LONG).show();

                        // show image
                        // change image
                        iv_Error.setVisibility(View.VISIBLE);
                        ll_action.setBackgroundColor(Color.WHITE);
                        iv_Error.setImageResource(R.drawable.empty);
                        swipereContainer.setRefreshing(false);
                        return;
                    }
                    else {

                        JSONArray my_action_items = apiResult.getJSONArray("records");
                        ArrayList<SubAction> action = new ArrayList<SubAction>();
                        SubAction subAction;
                        ActionModel ad;
                        SubAction [] st;
                        for(int i = 0;i<my_action_items.length();i++){

                             JSONObject obj = my_action_items.getJSONObject(i);
                             String id_main = obj.getString("id");
                             int numb_of_step = obj.getInt("numb_of_step");
                             String title_main = obj.getString("name");

                             JSONObject sub_obj= apiResult.getJSONObject(id_main);
                             int j = 0;


                            st =  null;
                            String id = null;

                            if (numb_of_step > 0 ) {
                                st = new SubAction[numb_of_step];
                            }else{

                            }

                            // add isComplete
                             for (j = 0 ; j < numb_of_step; j++){

                                 String name = sub_obj.getString(""+j);
                                 Boolean isComplete = sub_obj.getBoolean("completed"+j);
                                 id = sub_obj.getString("step_id"+j);

                                 subAction = new SubAction(name,id,isComplete);
                                 action.add(subAction);
                                 st[j] = subAction;


                             }

                            ad = new ActionModel((st == null) ? st:st.clone(),id_main,numb_of_step,title_main,false,false);
                            data.add(ad);
                            actionRecyclerAdapter.notifyDataSetChanged();

                        }

                        pb.setVisibility(View.GONE);
                        swipereContainer.setRefreshing(false);

                    }



                }
                catch (JSONException e) {
                    e.printStackTrace();
                    pb.setVisibility(View.GONE);
                    
                    iv_Error.setVisibility(View.VISIBLE);
                    iv_Error.setImageResource(R.drawable.error);
                    ll_action.setBackgroundColor(Color.WHITE);
                    swipereContainer.setRefreshing(false);
                    // error here
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.i("request failed", "failed");
                pb.setVisibility(View.GONE);

                iv_Error.setVisibility(View.VISIBLE);
                iv_Error.setImageResource(R.drawable.error);
                ll_action.setBackgroundColor(Color.WHITE);
                swipereContainer.setRefreshing(false);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), "Time-out", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getActivity(), "Request can't be Completed, unauthorize.", Toast.LENGTH_LONG).show();
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

        // rv adapter
        actionRecyclerAdapter = new ActionRecyclerAdapter(getActivity(),data);
        aRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        aRecycleView.setAdapter(actionRecyclerAdapter);


    }

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


}
