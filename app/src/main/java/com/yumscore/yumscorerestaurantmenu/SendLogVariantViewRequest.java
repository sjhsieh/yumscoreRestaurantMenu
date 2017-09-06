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
 * Created by steve on 5/1/17.
 */

public class SendLogVariantViewRequest extends AsyncTask<String, Void, String> {
    private static final String TAG = "SendLogVariantViewReque";

    private static final String LOG_VARIANT_VIEW_URL = "http://raddish.yumscore.com/api/v1/business/logVariantView";
    private static final String LOG_VARIANT_VIEW_PARAMS = "restaurantId=%s&menuVariantId=%s&device=android";

    public SendLogVariantViewRequest() {
    }

    @Override
    protected String doInBackground(String... params) {
        String response = sendLogVariantViewRequest(params[0], params[1]);
        return response;
    }

    private String sendLogVariantViewRequest(String restaurantId, String menuVariantId){
        try{
            URL url = new URL(LOG_VARIANT_VIEW_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            String params = String.format(LOG_VARIANT_VIEW_PARAMS, restaurantId, menuVariantId);
            Log.d(TAG, "sendLogVariantViewRequest: " + params);
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
            Log.d(TAG, "sendLogVariantViewRequest: " + result);
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
