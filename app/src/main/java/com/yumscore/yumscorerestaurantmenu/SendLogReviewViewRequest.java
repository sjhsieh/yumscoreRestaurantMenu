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
 * Created by steve on 5/16/17.
 */

public class SendLogReviewViewRequest extends AsyncTask<String, Void, String> {
    private static final String TAG = "SendLogReviewViewReques";

    private static final String LOG_REVIEW_VIEW_URL = "http://raddish.yumscore.com/api/v1/logReviewView";
    private static final String LOG_REVIEW_VIEW_PARAMS = "usertoken=%s&reviewId=%s";

    public SendLogReviewViewRequest() {
    }

    @Override
    protected String doInBackground(String... params) {
        String response = sendLogReviewViewRequest(params[0], params[1]);
        return response;
    }

    private String sendLogReviewViewRequest(String usertoken, String reviewId){
        try{
            URL url = new URL(LOG_REVIEW_VIEW_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            String params = String.format(LOG_REVIEW_VIEW_PARAMS, usertoken, reviewId);
            Log.d(TAG, "sendLogReviewViewRequest: " + params);
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
            Log.d(TAG, "sendLogReviewViewRequest: " + result);
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
