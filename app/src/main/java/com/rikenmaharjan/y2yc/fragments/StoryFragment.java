package com.rikenmaharjan.y2yc.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * Created by Riken on 3/24/18.
 */

public class StoryFragment extends BaseFragment {

    private TextView txtBedR;
    private TextView txtLockerR;
    private TextView txtMinor;
    private TextView txtMajor;
    private TextView txtDayR;
    private TextView txtDay;
    private TextView txtNITR;
    private String id;
    private String name;

    public static StoryFragment newInstance(){
        return new StoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_story,container,false);

        SessionManager session = new SessionManager(getActivity());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        name = user.get(SessionManager.KEY_NAME);

        // email
        id = user.get(SessionManager.KEY_ID);

        txtBedR = (TextView) view.findViewById(R.id.txtBedR);
        txtDayR = (TextView) view.findViewById(R.id.txtDayR);
        txtDay = (TextView) view.findViewById(R.id.txtDay);
        txtLockerR = (TextView) view.findViewById(R.id.txtLockerR);
        txtMinor = (TextView) view.findViewById(R.id.txtMinor);
        txtMajor = (TextView) view.findViewById(R.id.txtMajor);
        txtNITR = (TextView) view.findViewById(R.id.txtNITR);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://y2y.herokuapp.com/detailuser/"+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request sucessful", response );
                try{
                    JSONObject apiResult = new JSONObject(response);
                    view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    // get bed assignment
                    txtBedR.setText(apiResult.getString("Bed_name"));
                    //get last day of stay
                    String date = apiResult.getString("Last_Day_Of_Stay");
                    date = date.replace("-", "");
                    Date now = new Date();
                    String currentDateTimeString = DateFormat.getInstance().format(now);
                    String[] currentDate = currentDateTimeString.split("/");
                    String month = currentDate[0];
                    String day = currentDate[1];
                    if(Integer.parseInt(month) < 10)
                        month = "0" + month;
                    if(Integer.parseInt(day)<10){
                        day = "0" + day;
                    }
                    String cdate = "20" + currentDate[2].split(" ")[0]+ month + day;
                    //compare the date with the current date
                    if(date.equals("N/A")) {
                        txtDayR.setText("N/A");
                    }
                    else {
                        if (Integer.parseInt(cdate) > Integer.parseInt(date))
                            txtDayR.setText("N/A");
                        else
                            txtDayR.setText(date);
                    }
                    //get locker number
                    txtLockerR.setText(apiResult.getString("Locker"));
                    //get number of warnings
                    txtMinor.setText(apiResult.getString("Minor_warning")+" Minor");
                    txtMajor.setText(apiResult.getString("Major_warning")+" Major");
                    //get nit
                    String nit = apiResult.getString("NIT");
                    if(nit.equals("0")){
                        nit = "No";
                    }
                    else
                        nit = "Yes";
                    txtNITR.setText(nit);
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
        });

        queue.add(stringRequest);
        Log.i("result",queue.toString());

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
