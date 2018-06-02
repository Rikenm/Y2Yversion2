package com.rikenmaharjan.y2yc.activities;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.rikenmaharjan.y2yc.utils.Events;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bikenmaharjan on 5/25/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    Context nContext;
    List<Events> data;
    Dialog eventDialog;

    public SessionManager session;
    String id;
    String name;
    String Jwt_Token = new String();
    String []array_date;

    public RecyclerViewAdapter(Context context, List<Events> lstEvents) {

        this.data = lstEvents;
        this.nContext = context;
        array_date = new String[13];

        array_date[1] = "JAN";
        array_date[2] = "FEB";
        array_date[3] = "MAR";
        array_date[4] = "APR";
        array_date[5] = "MAY";
        array_date[6] = "JUN";
        array_date[7] = "JUL";
        array_date[8] = "AUG";
        array_date[9] = "SEP";
        array_date[10] = "OCT";
        array_date[11] = "NOV";
        array_date[12] = "DEC";
        array_date[0] = "JAN";


    }

    ////////// Generic class ////////////////////

    // TODO: Your code here
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v;


        v = LayoutInflater.from(nContext).inflate(R.layout.event_cell, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);


        eventDialog = new Dialog(nContext);
        eventDialog.setContentView(R.layout.dialog_eventdetail);


        vHolder.cb_rsvp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                // if clicked on event then
                if (b) {
                    try {
                        post("addUser",data.get(vHolder.getAdapterPosition()).getID());

                        // this works
                        // reload the stuff
                        data.get((vHolder.getAdapterPosition())).setRSVP(true);
                        notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    // change this
                    try {
                        post("removeUser",data.get(vHolder.getAdapterPosition()).getID());
                        data.get((vHolder.getAdapterPosition())).setRSVP(false);
                        notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        //
        vHolder.event_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // input here for dialog


                Button dialog_btn_rsvp = (Button) eventDialog.findViewById(R.id.btn_rsvp);
                Button dialog_btn_cancel = (Button) eventDialog.findViewById(R.id.btn_cancel);
                TextView tv_description = (TextView) eventDialog.findViewById(R.id.tv_description);
                TextView tv_title = (TextView) eventDialog.findViewById(R.id.txt_event);

                tv_description.setText((CharSequence) data.get(vHolder.getAdapterPosition()).getDescription());

                // CHECK
                tv_title.setText((CharSequence) data.get(vHolder.getAdapterPosition()).getTitle());
                eventDialog.show();

                //////
                dialog_btn_rsvp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("tag", "rsvp");
                        // post method here [done]
                        // also checkbox true
                        try {
                            // reload the stuff
                            post("addUser",data.get(vHolder.getAdapterPosition()).getID());
                            data.get((vHolder.getAdapterPosition())).setRSVP(true);
                            vHolder.cb_rsvp.setChecked(true);

                            notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        eventDialog.dismiss();
                    }
                });

                dialog_btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("tag", "cancel");
                        eventDialog.dismiss();
                    }
                });
                //////

            }
        });

        return vHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_location.setText(data.get(position).getLocation());

        holder.tv_time.setText(data.get(position).getTime());

        holder.tv_title.setText(data.get(position).getTitle());


        // month and date
        String date = data.get(position).getDate();
        String [] arr = date.split("-");
        String month = array_date[ Integer.parseInt(arr[0])];
        holder.tv_month.setText(month);
        holder.tv_day.setText(arr[1]);


        // particular cells

        holder.cb_rsvp.setOnCheckedChangeListener(null);
        holder.cb_rsvp.setChecked(data.get(position).getRSVP());

        holder.cb_rsvp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                // if clicked on event then
                if (isChecked) {
                    try {
                        post("addUser",data.get(position).getID());

                        // this works
                        // reload the stuff
                        data.get(position).setRSVP(true);
                        notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    // change this
                    try {
                        post("removeUser",data.get(position).getID());
                        data.get(position).setRSVP(false);
                        notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        //vHolder.cb_rsvp.setChecked(true);
        Log.i("Info","goes");




    }

    @Override
    public int getItemCount() {

        return data.size();
    }


    // all the initially binding goes
    // TODO: DO binding
    public static class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView tv_title;
        private TextView tv_location;
        private TextView tv_time;
        private CheckBox cb_rsvp;
        private LinearLayout event_layout;
        private TextView tv_day;
        private TextView tv_month;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.title);
            tv_location = (TextView) itemView.findViewById(R.id.location);
            tv_time = (TextView) itemView.findViewById(R.id.time);
            cb_rsvp = (CheckBox) itemView.findViewById(R.id.cb);
            event_layout = (LinearLayout) itemView.findViewById(R.id.ll_event_cell);

            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_month = (TextView) itemView.findViewById(R.id.tv_month);

        }


    }


    // TODO:- change this to post json body
    public void post(String flag, String ID) throws JSONException {

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
        RequestQueue requestQueue = Volley.newRequestQueue(nContext);
        String url = "https://y2y.herokuapp.com/events";


        // find the structure
        JSONObject jsonBody = new JSONObject();
        // set event in the data model
        jsonBody.put("eventId", ID);
        jsonBody.put("flag", flag);
        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Toast.makeText(nContext, "Feedback Sent!!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                Toast.makeText(nContext, "There is a problem, Please check your internet", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();

                String token = Jwt_Token;
                String auth = "bearer " + token;
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
                    Log.i("response", response.toString());
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);

//===================

    }
}
