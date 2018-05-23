package com.rikenmaharjan.y2yc.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.graphics.Typeface;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import com.rikenmaharjan.y2yc.R;
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
import com.rikenmaharjan.y2yc.utils.SessionManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// FOLLOWED THIS TUTORIAL: https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
// And referenced this: http://helloiamandroid.blogspot.com/2012/11/hello-android-developers_29.html

public class ViewActionFragment extends BaseFragment {
    //BaseFragmentActivity calls the following method and create LoginFragment to add to this activity

    public SessionManager session;
    static String id;
    String name;

    private
    ExpandableListView actions;
    ExpandableListAdapter adapter;
    List<String> Header;
    HashMap<String, List<String>> Child;
    TextView txtview;

    public View rootView;
    public static List<String> action_item_ids = new ArrayList<>();
    public static List<String[]> action_item_step_ids = new ArrayList<>();
    public static List<Integer> action_item_num_steps = new ArrayList<>();
    public static EditText reason;
    public static Button save_reason;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        session = new SessionManager(getActivity());

        session.checkLogin();
        //this takes you to login page if you are not logged in



        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        name = user.get(SessionManager.KEY_NAME);

        // email
        id = user.get(SessionManager.KEY_ID);

       // this makes user that every fragment gets id and name of the user



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_view_action,container,false);
        txtview = rootView.findViewById(R.id.txtView);
        txtview.setText("Here are your ongoing goals and action items. We would love to hear any updates or progress!");
        actions = rootView.findViewById(R.id.actions);
        reason = rootView.findViewById(R.id.action_reason);
        save_reason = rootView.findViewById(R.id.btnreason);
        reason.setVisibility(View.INVISIBLE);
        save_reason.setVisibility(View.INVISIBLE);
        prepareListData();
        Log.d("header",Header.toString());
        Log.d("child",Child.toString());


        actions.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        actions.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        actions.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        actions.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        return rootView;
    }

    public void prepareListData() {
        Header = new ArrayList<String>();
        Child = new HashMap<String, List<String>>();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        session = new SessionManager(getActivity());

        session.checkLogin();
        //this takes you to login page if you are not logged in



        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        name = user.get(SessionManager.KEY_NAME);

        // email
        id = user.get(SessionManager.KEY_ID);

        String url = "https://y2y.herokuapp.com/actionitems/"+id;


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
                        rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "There are currently no action items planned.", Toast.LENGTH_LONG).show();
                        StoryFragment story = new StoryFragment();
                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(getId(), story);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return;
                    }
                    JSONArray my_action_items = apiResult.getJSONArray("records");
                    for (int i = 0; i < num_action_items; i++) {
                        Header.add(my_action_items.getJSONObject(i).getString("name"));
                        num_steps = Integer.parseInt(my_action_items.getJSONObject(i).getString("numb_of_step"));
                        action_item_num_steps.add(num_steps);
                        String[] steps = new String[num_steps];
                        String[] step_item_ids = new String[num_steps];
                        String current_action_id = my_action_items.getJSONObject(i).getString("id");
                        action_item_ids.add(current_action_id);
                        JSONObject current_action = apiResult.getJSONObject(current_action_id);
                        for (int j = 0; j < num_steps; j++) {
                            steps[j] = (current_action.getString(Integer.toString(j+1)));
                            step_item_ids[j] = current_action.getString("step_id"+Integer.toString(j+1));
                        }
                        childList.add(steps);
                        action_item_step_ids.add(step_item_ids);
                        List<String> temp = Arrays.asList(childList.get(i));
                        Child.put(Header.get(i), temp);

                        adapter = new MyCustomAdapter(getActivity(), Header, Child);
                        actions.setAdapter(adapter);
                        rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
        });

        queue.add(stringRequest);
        Log.i("result",queue.toString());
    }

    public static List<String> action_item_ids_Data() {
        return action_item_ids;
    }

    public static List<Integer> get_num_steps() { return action_item_num_steps; }

    public static Boolean no_action_item() { return action_item_ids.size() == 0; }
}


class MyCustomAdapter extends BaseExpandableListAdapter {

    private
    Context context;
    List<String> Header;
    HashMap<String, List<String>> Child;
    final ViewActionFragment frag = new ViewActionFragment();

    public MyCustomAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.Header = listDataHeader;
        this.Child = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.Child.get(this.Header.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        Log.d("check", String.valueOf(groupPosition));
        if (frag.no_action_item()) {
            Toast.makeText(context, "There are currently no action items planned.", Toast.LENGTH_LONG).show();
            return null;
        }
        else if (frag.get_num_steps().get(groupPosition) == 0) {
            return null;
        }

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        TextView txtListChild = convertView.findViewById(R.id.myListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.Child.get(this.Header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.Header.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.Header.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (frag.no_action_item()) {
            Toast.makeText(context, "There are currently no action items planned.", Toast.LENGTH_LONG).show();
            return null;
        }
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

            TextView myListHeader = convertView.findViewById(R.id.myListHeader);
            CheckBox checkBox1 = convertView.findViewById(R.id.checkBox1);
            CheckBox checkBox2 = convertView.findViewById(R.id.checkBox2);
            final EditText reason = ViewActionFragment.reason;
            final Button save_reason = ViewActionFragment.save_reason;
            myListHeader.setTypeface(null, Typeface.BOLD);
            myListHeader.setText(headerTitle);


            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton complete, boolean isChecked) {

                    if (isChecked == true) {
                        reason.setVisibility(View.VISIBLE);
                        save_reason.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Please explain your action in the comment box below.", Toast.LENGTH_LONG).show();

                        save_reason.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                try {
                                    final RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    String url = "https://y2y.herokuapp.com/actionitems";
                                    String current_action_id = frag.action_item_ids_Data().get(groupPosition);
                                    JSONObject jo = new JSONObject();
                                    jo.put("flag", "Completed");
                                    jo.put("actionid", current_action_id);

                                    jo.put("comment", reason.getText().toString());
                                    final String requestBody = jo.toString();
                                    Toast.makeText(context, "Information Saved", Toast.LENGTH_SHORT).show();

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

                                reason.setText(null);
                                Header.remove(groupPosition);
                                reason.setVisibility(View.INVISIBLE);
                                save_reason.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else if (isChecked == false) {
                        reason.setVisibility(View.INVISIBLE);
                        save_reason.setVisibility(View.INVISIBLE);
                    }
                }
            });

            checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton drop, boolean isChecked) {

                    if (isChecked == true) {
                        reason.setVisibility(View.VISIBLE);
                        save_reason.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Please explain your action in the comment box below.", Toast.LENGTH_LONG).show();
                        save_reason.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                try {
                                    final RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    String url = "https://y2y.herokuapp.com/actionitems";
                                    String current_action_id = frag.action_item_ids_Data().get(groupPosition);
                                    JSONObject jo = new JSONObject();
                                    jo.put("flag", "Dropped");
                                    jo.put("actionid", current_action_id);
                                    jo.put("comment", reason.getText().toString());
                                    final String requestBody = jo.toString();
                                    Toast.makeText(context, "Information Saved", Toast.LENGTH_SHORT).show();

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.i("VOLLEY", response);
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

                                reason.setText(null);
                                Header.remove(groupPosition);
                                reason.setVisibility(View.INVISIBLE);
                                save_reason.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else if (isChecked == false) {
                        reason.setVisibility(View.INVISIBLE);
                        save_reason.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        return convertView;


    }
}
