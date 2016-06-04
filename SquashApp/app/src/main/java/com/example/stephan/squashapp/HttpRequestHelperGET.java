package com.example.stephan.squashapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Stephan on 31-5-2016.
 *
 */

public class HttpRequestHelperGET extends AsyncTask<URL, Integer, ArrayList<Training>> {

    ///private static final String url1 = "https://api.myjson.com/bins/50g3w";   // url
    private static final String url1 = "http://schietsquash.nl/api_trainingen/";   // url
    public AsyncResponse delegate = null;       // initialize to null;

    /**
     * Function in the activity to give the information.
     * ! So these functions must be present!
     */
    public interface AsyncResponse{
        void processFinish(ArrayList<Training> output);
    }
    /**
     * Set activity to send response.
     */
    public HttpRequestHelperGET(AsyncResponse delegate){
        this.delegate = delegate;
    }

    /**
     * The HttpRequestHelper
     */
    protected ArrayList<Training> doInBackground(URL... urls) {

        // Initialize
        String returnValue = "";

        // Get complete url
        String completeUrl = url1;

        // Initialize url to null
        URL url = null;
        try {
            url = new URL(completeUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // make connection.
        HttpURLConnection connection;
        if(url != null){

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");


                // read response
                Integer response = connection.getResponseCode();

                // handle errors
                if(response >= 200 && response < 300){
                    // read the data
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    // save all data
                    while((line = br.readLine()) != null){
                        returnValue = returnValue + line;
                    }

                }
                // get error
                else{
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String line;
                    while((line = br.readLine()) != null){
                        returnValue = returnValue + line;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // create an empty list
        ArrayList<Training> trainingList = new ArrayList<>();

        // procces given data.
        try {
            // make a jsonObject
            JSONArray jsonArray = new JSONArray(returnValue);

            Log.v("json", returnValue);
            // get all trainings
            for(int n = 0; n < jsonArray.length(); n++)
            {
                JSONObject object = jsonArray.getJSONObject(n);
                Log.v("jsonobject", object.toString());

                String date = object.getString("Date");
                String info = object.getString("Kind of training");
                String start = object.getString("Start training");
                String end = object.getString("End training");
                String by = object.getString("By who?");
                Integer maxPlayers = object.getInt("max players");
                Integer currentPlayers = object.getInt("current players");
                ArrayList<String> registered = new ArrayList<>();

                for (int players = 0; players < currentPlayers; players++){
                    registered.add(object.getString("registered" + players));
                }
                //Training training = new Training(date, info, start, end, by,
                        //maxPlayers, currentPlayers, registered);

                //trainingList.add(training);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("jsonerror", returnValue);
        }

        return trainingList;
    }

    protected void onPostExecute(ArrayList<Training> trainingList) {
        delegate.processFinish(trainingList);
    }


}
