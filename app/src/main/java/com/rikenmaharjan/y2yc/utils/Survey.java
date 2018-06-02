package com.rikenmaharjan.y2yc.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rikenmaharjan.y2yc.fragments.HomeFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Riken on 6/2/18.
 */

public class Survey {

    int _rating;
    String _comment;
    String Jwt_Token;
    String response;
    Context _context;

    public Survey(int rating, String Comment, String Token){
        _comment = Comment;
        _rating = rating;
        Jwt_Token = Token;

    }

    public void submitSurvey(Context context){


        _context = context;

        new Survey.MyAsyncTaskget().execute("hello", "hello", "hello");

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

            String json =  "{\"rating\": \""+_rating+"\",\"comment\":\""+_comment+"\"}";
            HttpURLConnection urlConnection = null;
            response = new String();


            try {
                URL url = new URL("https://y2y.herokuapp.com/edituser");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");

                urlConnection.setRequestProperty("Authorization", "bearer "+ Jwt_Token);


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

                Log.i("isSucess", "success");
                Toast.makeText(_context, "Saved", Toast.LENGTH_SHORT).show();
            }
            else if (result2.equals("error")){

                Log.i("isSuccess", "Error");
                Toast.makeText(_context, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
