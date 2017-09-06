package com.yumscore.yumscorerestaurantmenu;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by steve on 3/18/17.
 */

public class SendLogDishViewRequest extends AsyncTask<String, Void, String> {
    private static final String TAG = "SendLogDishViewRequest";

    private static final String LOG_DISH_VIEW_URL = "http://raddish.yumscore.com/api/v1/business/logDishView";
    private static final String LOG_DISH_VIEW_PARAMS = "restaurantId=%s&sectionId=%s&dishId=%s&device=android";

    public SendLogDishViewRequest() {
    }

    @Override
    protected String doInBackground(String... params) {
        String response = sendLogDishViewRequest(params[0], params[1], params[2]);
        return response;
    }

    private String sendLogDishViewRequest(String restaurantId, String sectionId, String dishId){
        try{
            URL url = new URL(LOG_DISH_VIEW_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            String params = String.format(LOG_DISH_VIEW_PARAMS, restaurantId, sectionId, dishId);
            Log.d(TAG, "sendLogDishViewRequest: " + params);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);
            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while(null != (line = reader.readLine())){
                result.append(line).append("\n");
            }
            Log.d(TAG, "sendLogDishViewRequest: " + result);
            return result.toString();
        }catch(MalformedURLException e){
            Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
        }catch(IOException e){
            Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());
        }catch(SecurityException e){
            Log.e(TAG, "downloadXML: Security Exception. Needs Permission? " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {

    }
}
