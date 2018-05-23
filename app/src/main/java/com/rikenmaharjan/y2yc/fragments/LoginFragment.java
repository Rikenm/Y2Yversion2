package com.rikenmaharjan.y2yc.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Riken on 3/18/18.
 */

public class LoginFragment extends BaseFragment {


    // using butterknife to make binding buttons easy.

    //username is rikenm and password is "password1234"

    @BindView(R2.id.fragment_login_userName)
    EditText mUSerNameEt;

    @BindView(R2.id.fragment_login_userPassword)
    EditText mUSerPasswordEt;

    @BindView(R2.id.fragment_login_register_button)
    Button mLoginButton;

    private Unbinder mUnbinder;




    // Session Manager Class
    SessionManager session;


    String response;



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

        return rootView;
    }


    @OnClick(R2.id.fragment_login_register_button)
    public void setmLoginButton(){

        if (!(mUSerNameEt.getText().toString().equals(""))&&(!(mUSerPasswordEt.getText().toString().equals("")))) {
            mLoginButton.setEnabled(false);
            new MyAsyncTaskgetNews().execute("hello", "hello", "hello");
        }
        else if (mUSerNameEt.getText().toString().equals("")){
            mUSerNameEt.setError("Enter FirstName");

        }
        else if(mUSerPasswordEt.getText().toString().equals("")){

            mUSerPasswordEt.setError("This Field Cannot be Empty");

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

                //URL url = new URL("http://155.41.34.62:3000/login"); //computerlab ip



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









        }
        protected void onPostExecute(String  result2) {

            //if (result2 == null) checks this

            JSONObject reader = null;
            String isvalid = null;
            String id = null;
            String name = null;
            try {
                reader = new JSONObject(result2);
                isvalid = reader.getString("isValid");
                id = reader.getString("id");
                name = reader.getString("name");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //creating session
            session.createLoginSession(name, id);




            //result2 = result2.substring(0, result2.length() - 1); //removing the null char

            Log.i("isValid", isvalid);


            if (isvalid.equals("invalid")) {

                Log.i("isValid", "Invalid");
                Toast.makeText(getActivity(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                mLoginButton.setEnabled(true);


            } else if (isvalid.equals("Network Error")) {
                Log.i("isValid", "Network Error");
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                mLoginButton.setEnabled(true);

            } else {




                Log.i("isValid", id);
                Intent i = (new Intent(getActivity(), Main2Activity.class));
                i.putExtra("id", id);
                i.putExtra("name",name);

                startActivity(i);
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
}
