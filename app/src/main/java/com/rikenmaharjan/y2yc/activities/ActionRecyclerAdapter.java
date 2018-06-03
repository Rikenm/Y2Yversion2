package com.rikenmaharjan.y2yc.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.utils.ActionModel;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by bikenmaharjan on 6/3/18.
 */

public class ActionRecyclerAdapter extends RecyclerView.Adapter<ActionRecyclerAdapter.MyViewHolder>{
    // global variables
    Context nContext;
    ArrayList<ActionModel> data;
    String id;
    String name;
    String Jwt_Token = new String();
    public SessionManager session;


    public ActionRecyclerAdapter(Context context,ArrayList<ActionModel> data){
        this.nContext = context;
        this.data = data;
    }

    // runs once
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(nContext).inflate(R.layout.action_cell, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        vHolder.ll_action.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("ll_action","open dialog with list of sub list");
                        // check if
//
//                        if (data.get(vHolder.getAdapterPosition()).getComplete()){
//
//                        }
//                        else if (data.get(vHolder.getAdapterPosition()).getDrop()){
//
//                        }
//                        else {
//                            // do nothing
//                        }


                    }
                }
        );
        return vHolder;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // runs multiple time
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tv_action.setText((CharSequence) data.get(position).getTitle());
        holder.cb_complete.setChecked(data.get(position).getComplete());
        holder.cb_drop.setChecked(data.get(position).getDrop());

        holder.cb_complete.setOnCheckedChangeListener(

                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){
                            // open comment from there post request
                            Log.i("cb_complete","open comment from there post request");
                            data.get(holder.getAdapterPosition()).setComplete(true);
                            id = data.get(holder.getAdapterPosition()).getId();

                            post("Completed",id,"test");
                            change(holder.getAdapterPosition());


                        }
                        else{
                            data.get(holder.getAdapterPosition()).setComplete(false);
                        }

                    }
                }
        );

        holder.cb_drop.setOnCheckedChangeListener(

                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (b) {
                            Log.i("cb_drop", "open comment from there post request");
                            data.get(holder.getAdapterPosition()).setDrop(true);
                            id = data.get(holder.getAdapterPosition()).getId();
                            change(holder.getAdapterPosition());
                            post("Dropped",id,"test");
                        }
                        else{
                            data.get(holder.getAdapterPosition()).setDrop(false);
                        }

                        notifyDataSetChanged();

                    }

                }
        );

    }

    // item size here
    @Override
    public int getItemCount() {
        return data.size();
    }


    // binding here from cell
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_action;
        CheckBox cb_complete;
        CheckBox cb_drop;
        LinearLayout ll_action;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_action = (TextView) itemView.findViewById(R.id.tv_action);
            cb_complete = (CheckBox) itemView.findViewById(R.id.cb_comp);
            cb_drop = (CheckBox) itemView.findViewById(R.id.cb_drop);
            ll_action = (LinearLayout) itemView.findViewById(R.id.ll_action_cell);

        }


    }


    // post
    public void post(String flag, String actionId,String comment){

        session = new SessionManager(nContext);

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // Get logged in user's user name
        name = user.get(SessionManager.KEY_NAME);

        // Get loged in user's user id
        id = user.get(SessionManager.KEY_ID);

        Jwt_Token = user.get(SessionManager.JWT_Token);

        //===================


        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(nContext);
            String url = "https://y2y.herokuapp.com/actionitems";
            String current_action_id = actionId;
            JSONObject jo = new JSONObject();
            jo.put("flag", flag);
            jo.put("actionid", current_action_id);
            jo.put("comment", comment);
            final String requestBody = jo.toString();
            Toast.makeText(nContext, "Information Saved", Toast.LENGTH_SHORT).show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

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
                        Log.i("response", response.toString());
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void change(int position){
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,data.size());
        notifyDataSetChanged();
    }

}
