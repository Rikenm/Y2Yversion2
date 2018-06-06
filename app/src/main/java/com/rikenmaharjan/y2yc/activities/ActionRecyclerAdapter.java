package com.rikenmaharjan.y2yc.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.rikenmaharjan.y2yc.fragments.ActionFragment;
import com.rikenmaharjan.y2yc.utils.ActionModel;
import com.rikenmaharjan.y2yc.utils.SessionManager;
import com.rikenmaharjan.y2yc.utils.SubAction;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.json.JSONArray;
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

public class ActionRecyclerAdapter extends RecyclerView.Adapter<ActionRecyclerAdapter.MyViewHolder> {
    // global variables
    Context nContext;
    ArrayList<ActionModel> data;
    String id;
    String name;
    String Jwt_Token = new String();
    public SessionManager session;
    public int globalPosition;
    public int globalSubPosition;
    Dialog subAction;
    RecyclerView rv;

    SubActionRAdapter sb;

    String comment;
    private String m_Text = "";
    private String sub_Text = "";

    public ActionRecyclerAdapter(Context context,ArrayList<ActionModel> data){
        this.nContext = context;
        this.data = data;
    }

    // runs once
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        View v;
        v = LayoutInflater.from(nContext).inflate(R.layout.action_cell, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        //TODO: show new dialog box that has recycle view


        subAction = new Dialog(nContext);
        subAction.requestWindowFeature(Window.FEATURE_NO_TITLE);
        subAction.setContentView(R.layout.dialog_subaction);

        vHolder.ll_action.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        //
                        Log.i("ll_action","open dialog with list of sub list");
                        rv = (RecyclerView) subAction.findViewById(R.id.rv_subAction);
                        rv.setLayoutManager(new LinearLayoutManager(nContext));

                        // find views from subActon
                        Button btn_ok = (Button) subAction.findViewById(R.id.btn_subAction_ok);
                        Button btn_cancel = (Button) subAction.findViewById(R.id.btn_subAction_cancel);
                        //when ok -- change and post to the server



                        // clear

                        btn_ok.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // get values and post
                                        List<String> ids = ((SubActionRAdapter) rv.getAdapter()).getId1();
                                        List<Integer> idices = ((SubActionRAdapter) rv.getAdapter()).getIndice();
                                        if (ids.size() > 0 && idices.size()>0) {
                                            Log.i("ar", ids + "");
                                            // post request
                                            // reload the main table
                                            String mainId = data.get(vHolder.getAdapterPosition()).getId();
                                            globalSubPosition = vHolder.getAdapterPosition();
                                            showSubActionDialog(parent,mainId,ids);
                                        }
                                        else{
                                            Toast.makeText(nContext,"You didn't select anything",Toast.LENGTH_SHORT);
                                        }
                                        subAction.dismiss();
                                    }
                                }
                        );

                        subAction.setCancelable(false);
                        subAction.show();


                        // when cancel -- don't change anything
                        btn_cancel.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        subAction.dismiss();
                                        // manual set this
                                        // works
                                        //List<String> ids = ((SubActionRAdapter) rv.getAdapter()).getId1();
                                        List<Integer> idices = ((SubActionRAdapter) rv.getAdapter()).getIndice();


                                        for (int i = 0; i < idices.size();i++ ) {
                                            int index = idices.get(i);
                                            ((SubActionRAdapter) rv.getAdapter()).data[index].setComplete(false);

                                        }



                                    }
                                }
                        );
                        // set adapter here // data -> data.get(viewType).getSubAction();
                        int value = vHolder.getAdapterPosition();
                        //ArrayList<SubAction> value1= data.get(vHolder.getAdapterPosition()).getSubAction();
                        SubAction []sb1 = data.get(vHolder.getAdapterPosition()).getSubAction();
                        sb = new SubActionRAdapter(nContext,data.get(vHolder.getAdapterPosition()).getSubAction());
                        rv.setAdapter(sb);
                        notifyDataSetChanged();

                    }
                }
        );
/// good
        vHolder.cb_complete.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){
                            // open comment from there post request
                            Log.i("cb_complete","open comment from there post request");
                            data.get(vHolder.getAdapterPosition()).setComplete(true);
                            id = data.get(vHolder.getAdapterPosition()).getId();
                            globalPosition = vHolder.getAdapterPosition();
                            showDialog(parent,"Completed",id,globalPosition);
                        }
                        else{
                            data.get(vHolder.getAdapterPosition()).setComplete(false);
                        }

                    }
                }
        );

        vHolder.cb_drop.setOnCheckedChangeListener(

                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (b) {
                            Log.i("cb_drop", "open comment from there post request");
                            data.get(vHolder.getAdapterPosition()).setDrop(true);
                            id = data.get(vHolder.getAdapterPosition()).getId();
                            globalPosition = vHolder.getAdapterPosition();
                            showDialog(parent,"Dropped",id,globalPosition);


                        }
                        else{
                            // error here
                            data.get(vHolder.getAdapterPosition()).setDrop(false);
                        }
                    }

                }
        );
//

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



    }

    // item size here
    @Override
    public int getItemCount() {
        return data.size();
    }



    // 2 Dialog box
    private void showDialog(ViewGroup parent, final String type,final String id, final int position) {

        View v;
        v = LayoutInflater.from(nContext).inflate(R.layout.dialog_action, parent, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(nContext);
        builder.setTitle("Add message");
        final EditText input = (EditText) v.findViewById(R.id.input);
        builder.setView(v);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                m_Text = input.getText().toString();
                post(type,id,m_Text,position); // <--- here
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                data.get(position).setComplete(false);
                data.get(position).setDrop(false);
                notifyDataSetChanged();

            }
        });

       builder.setCancelable(false);
        builder.show();

    }
    private void showSubActionDialog(ViewGroup parent,final String mainId,final List<String> ids) {

        View v;
        v = LayoutInflater.from(nContext).inflate(R.layout.dialog_action, parent, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(nContext);
        builder.setTitle("Add message");
        final EditText input = (EditText) v.findViewById(R.id.input);
        builder.setView(v);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                sub_Text = input.getText().toString();
                postSubAction(mainId,ids,sub_Text);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                List<Integer> idices = ((SubActionRAdapter) rv.getAdapter()).getIndice();
                for (int i = 0; i < idices.size();i++ ) {
                    int index = idices.get(i);
                    ((SubActionRAdapter) rv.getAdapter()).data[index].setComplete(false);
                }
                notifyDataSetChanged();

            }
        });

        builder.setCancelable(false);
        builder.show();

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
    public void post(String flag, String actionId, String comment, final int position){

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
                    change();
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

    // post for subaction
    public void postSubAction(String mainActionId,List<String> listOfIDS,String comment){

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

            String [] arr_IDS = new String[listOfIDS.size()];
            for (int i=0;i<listOfIDS.size();i++){
                arr_IDS[i] = listOfIDS.get(i);
            }

            Log.i("main",""+mainActionId);
            Log.i("SIZE",""+listOfIDS.size());
            Log.i("comment",""+comment);
            Log.i("records", ""+ arr_IDS);
            String url = "https://y2y.herokuapp.com/actionitemstep";
            String current_action_id = mainActionId;
            JSONObject jo = new JSONObject();
            jo.put("size", listOfIDS.size());
            jo.put("actionid", current_action_id);
            jo.put("comment", comment);
            jo.put("records", new JSONArray(listOfIDS));
            final String requestBody = jo.toString();
            Toast.makeText(nContext, "Information Saved", Toast.LENGTH_SHORT).show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response.toString());
                    changeSubAction();
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

    public void change(){
        data.remove(globalPosition);
        notifyItemRemoved(globalPosition);
        notifyItemRangeChanged(globalPosition,data.size());
        notifyDataSetChanged();
    }

    public void changeSubAction(){

        int count = 0;
        int length = ((SubActionRAdapter) rv.getAdapter()).data.length;
        for (int j = 0;j<length;j++){
            Boolean flag = ((SubActionRAdapter) rv.getAdapter()).data[j].getComplete();
            if(flag == false){
                count++;
            }

        }

        // lock
        if (count == 0){
            Log.i("subAction","goes in");
            data.remove(globalSubPosition);
            notifyItemRemoved(globalSubPosition);
            notifyItemRangeChanged(globalSubPosition,data.size());
            notifyDataSetChanged();
        }

    }

}
