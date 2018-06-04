package com.rikenmaharjan.y2yc.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionFragment extends BaseFragment {


    private RecyclerView aRecycleView ;
    private ActionRecyclerAdapter actionRecyclerAdapter;
    private String id;
    private String name;
    private String Jwt_Token;
    private ArrayList<ActionModel> data;

    public ActionFragment() {
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

        loadData();
        // send data
        actionRecyclerAdapter = new ActionRecyclerAdapter(container.getContext(),data);
        aRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        aRecycleView.setAdapter(actionRecyclerAdapter);

        return v;
    }

    public void loadData(){

        String url = "https://y2y.herokuapp.com/actionitems/";

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        data.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request successful", response );

                try{
                    JSONObject apiResult = new JSONObject(response);
                    int num_action_items;
                    int num_steps;

                    List<String[]> childList = new ArrayList<>();
                    num_action_items = Integer.parseInt(apiResult.getString("size"));

                    if (num_action_items == 0) {
                        Toast.makeText(getActivity(), "There are currently no action items planned.", Toast.LENGTH_LONG).show();
                        StayFragment story = new StayFragment();
                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(getId(), story);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return;
                    }
                    else {
                        JSONArray my_action_items = apiResult.getJSONArray("records");

                        int i = 0;
                        for(i = 0;i<my_action_items.length();i++){

                             JSONObject obj = my_action_items.getJSONObject(i);
                             String id_main = obj.getString("id");
                             int numb_of_step = obj.getInt("numb_of_step");
                             String title_main = obj.getString("name");

                             JSONObject sub_obj= apiResult.getJSONObject(id_main);
                             int j = 0;

                            ArrayList<SubAction> action = new ArrayList<SubAction>();
                             for (j = 0 ; j < numb_of_step; j++){
                                 String name = sub_obj.getString(""+j);
                                 Boolean isComplete = sub_obj.getBoolean("completed"+j);
                                 String id = sub_obj.getString("step_id"+j);

                                 SubAction subAction = new SubAction(name,id,isComplete);
                                 action.add(subAction);

                             }

                            ActionModel ad = new ActionModel(action,id_main,numb_of_step,title_main,false,false);
                            data.add(ad);
                            actionRecyclerAdapter.notifyDataSetChanged();

                        }

                    }



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
        Log.i("result",queue.toString());
    }

}
