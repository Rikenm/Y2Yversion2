package com.rikenmaharjan.y2yc.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.R2;

import com.rikenmaharjan.y2yc.activities.Main2Activity;

import com.rikenmaharjan.y2yc.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Riken on 3/18/18.
 */

public class  LoginFragment extends BaseFragment {


    //TODO:- change this when Y2Y buys the domain
    final String URL = "https://y2yweb.herokuapp.com";

    TextView btn_newPassword;
    // using butterknife to make binding buttons easy.

    //username is rikenm and password is "password1234"


    @BindView(R2.id.fragment_login_userName)
    EditText mUSerNameEt;

    @BindView(R2.id.fragment_login_userPassword)
    EditText mUSerPasswordEt;

    @BindView(R2.id.fragment_login_register_button)
    Button mLoginButton;


    private Unbinder mUnbinder;
    private ProgressBar pb_login;


    // Session Manager Class
    SessionManager session;
    String response;

    TextInputLayout inputLayoutName;
    TextInputLayout inputLayoutusernameName;



    public static LoginFragment newInstance(){
        return new LoginFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        session = new SessionManager(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_login,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        inputLayoutName = (TextInputLayout) rootView.findViewById(R.id.input_layout_name);
        inputLayoutusernameName = (TextInputLayout) rootView.findViewById(R.id.input_layout_username);
        pb_login = (ProgressBar) rootView.findViewById(R.id.pb_login);
        btn_newPassword = (TextView) rootView.findViewById(R.id.fragment_forget_textView);

        pb_login.getIndeterminateDrawable().setColorFilter(0xFFcc0000,
                android.graphics.PorterDuff.Mode.MULTIPLY);



        // segue
        btn_newPassword.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //ResetFragment rf = new ResetFragment();
                        //replaceFragment(ResetFragment.newInstance());
                        Intent i =  new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(URL));
                        startActivity(i);

                    }
                }
        );




        return rootView;
    }


    @OnClick(R2.id.fragment_login_register_button)
    public void setmLoginButton(){

        if (!(mUSerNameEt.getText().toString().equals(""))&&(!(mUSerPasswordEt.getText().toString().equals("")))) {
            mLoginButton.setEnabled(false);


            pb_login.setVisibility(View.VISIBLE);
            new MyAsyncTaskgetNews().execute("hello", "hello", "hello");




        }
        else if (mUSerNameEt.getText().toString().equals("")){
            mUSerNameEt.setError("This field cannot be empty");
            pb_login.setVisibility(View.INVISIBLE);

        }
        else if(mUSerPasswordEt.getText().toString().equals("")){

            mUSerPasswordEt.setError("This field cannot be empty");
            pb_login.setVisibility(View.INVISIBLE);

        }




    }

    // derived from stackoverflow

    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub

            //String json =  "{\"username\": \"sampada\",\"password\":\"password123\"}";
            String json =  "{\"username\": \""+mUSerNameEt.getText().toString()+"\",\"password\":\""+mUSerPasswordEt.getText().toString()+"\"}";
            HttpURLConnection urlConnection = null;
            response = new String();




            try {

                URL url = new URL("https://y2y.herokuapp.com/login");



                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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

                        Log.e("nameisvali",mUSerNameEt.getText().toString());
                        Log.i("HTTP Client", "Received String : " + sb.toString());
                        response = sb.toString();
                        Log.i("Json",(String)(sb.getClass().getName()));
                        //return received string
                        return sb.toString();

                     default:
                         Log.i("httpstatus","here");
                         return null;





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



        }
        protected void onPostExecute(String  result2) {

            if (result2 == null){


                Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                mLoginButton.setEnabled(true);
                pb_login.setVisibility(View.INVISIBLE);



            }
            else{

                //if (result2 == null) checks this

                JSONObject reader = null;
                String isvalid = null;
                String id = null;
                String name = null;
                String token = null;
                try {
                    reader = new JSONObject(result2);
                    isvalid = reader.getString("isValid");
                    id = reader.getString("id");
                    name = reader.getString("name");
                    token = reader.getString("token");

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                //result2 = result2.substring(0, result2.length() - 1); //removing the null char

                Log.i("isValid", isvalid);


                if (isvalid.equals("invalid")) {

                    Log.i("isValid", "Invalid");
                    Toast.makeText(getActivity(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    mLoginButton.setEnabled(true);
                    pb_login.setVisibility(View.INVISIBLE);


                }
                else {

                    Log.i("inside transition", "inside transition");
                    //creating session
                    session.createLoginSession(name, id,token);

                    Log.i("isValid", id);
                    Intent i = (new Intent(getActivity(), Main2Activity.class));
                    i.putExtra("id", id);
                    i.putExtra("name",name);

                    startActivity(i);
                }

            }

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onStop(){
        Tovuti.from(getActivity()).stop();
        super.onStop();
    }


    // Replace Fragment
    public void replaceFragment(Fragment someFragment) {


        //FragmentManager fragmentManager = getFragmentManager(); // static variable
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_fragment_base_fragmentContainer, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
