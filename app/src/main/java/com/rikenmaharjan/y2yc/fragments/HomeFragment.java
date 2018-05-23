package com.rikenmaharjan.y2yc.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.R2;
//import com.rikenmaharjan.y2yc.activities.HomeActivity;
import com.rikenmaharjan.y2yc.activities.Main2Activity;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Riken on 3/18/18.
 */

public class HomeFragment extends BaseFragment {



    String rate= "0";



    @BindView(R2.id.fragment_home_commentEt)
    EditText mCommentEt;

    @BindView(R2.id.fragment_home_btnsave)
    Button mSaveButton;




    @BindView(R2.id.fragment_home_introEt)
    TextView introTxt;




    String response;

    private Unbinder mUnbinder;

    String id;
    String name;
    SessionManager session;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);

        mUnbinder = ButterKnife.bind(this,rootView);



        SmileRating smileRating = (SmileRating) rootView.findViewById(R.id.smile_rating);


        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                String TAG = "tag";

                switch (smiley) {


                    case SmileRating.TERRIBLE:
                        rate ="1";
                        Log.i("tag", "Bad");
                        break;
                    case SmileRating.BAD:
                        rate ="2";
                        Log.i(TAG, "Good");
                        break;
                    case SmileRating.OKAY:
                        rate ="3";
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.GOOD:
                        rate ="4";
                        Log.i(TAG, "Okay");
                        break;
                    case SmileRating.GREAT:
                        rate ="5";
                        Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



    }

    @Override
    public void onResume() {
        super.onResume();

        session = new SessionManager(getActivity());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        name = user.get(SessionManager.KEY_NAME);

        // email
        id = user.get(SessionManager.KEY_ID);

        introTxt.setText("Hello, "+name);

    }

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }




    @OnClick(R2.id.fragment_home_btnsave)
    public void setmSaveButton(){

        hideKeyboard(getActivity());

        String sComment = mCommentEt.getText().toString();

        if(sComment.equals("")) {
            mCommentEt.setError("Your message");
            //return;
        }
        else{


            Log.i("hell0","enter");


            //Log.i("Id",id);

            new HomeFragment.MyAsyncTaskget().execute(id, "hello", "hello");


            mCommentEt.setText("");

        }

    }



    public class MyAsyncTaskget extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("param",params[0]+"."+params[1]);

            String json =  "{\"rating\": \""+rate+"\",\"comment\":\""+mCommentEt.getText().toString()+"\",\"id\":\""+id+"\"}";
            HttpURLConnection urlConnection = null;
            response = new String();


            try {
                URL url = new URL("https://y2y.herokuapp.com/edituser");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");

                if (json != null) {
                    //set the content length of the body
                    urlConnection.setRequestProperty("Content-length", json.getBytes().length + "");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setUseCaches(false);

                    //send the json as body of the request
                    OutputStream outputStream = urlConnection.getOutputStream();
                    outputStream.write(json.getBytes("UTF-8"));
                    outputStream.close();
                }



                //urlConnection.setConnectTimeout(7000);
                urlConnection.connect();
                int status = urlConnection.getResponseCode();
                Log.i("HTTP Client", "HTTP status code : " + status);
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        bufferedReader.close();
                        Log.i("HTTP Client", "Received String : " + sb.toString());
                        response = sb.toString();
                        Log.i("Json",(String)(sb.getClass().getName()));
                        //return received string
                        return sb.toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                //end connection
                urlConnection.disconnect();
            }

            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try {
                //display response dataken


            } catch (Exception ex) {
            }


        }
        protected void onPostExecute(String  result2){

            result2 = result2.substring(0,result2.length()-1); //removing the null char

            Log.i("isSuccess", result2);

            if(result2.equals("success") ) {

                Log.i("isSucess", "sucess");
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            }
            else if (result2.equals("error")){

                Log.i("isSuccess", "Error");
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
