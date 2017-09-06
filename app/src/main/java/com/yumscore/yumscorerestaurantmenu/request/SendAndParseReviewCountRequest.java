package com.yumscore.yumscorerestaurantmenu.request;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by steve on 6/14/17.
 */

public class SendAndParseReviewCountRequest extends AsyncTask<String, Void, String> {
    private static final String TAG = "SendAndParseReviewCount";

    private static final String RESPONSE_PARAM_NUMREVIEWS = "numReviews";

    private static final String SEND_REVIEW_COUNT_URL = "http://raddish.yumscore.com/api/v1/getNumReviewsForDish?restaurantId=%s&dishId=%s";

    private TextView textView;

    public SendAndParseReviewCountRequest(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = sendReviewCountRequest(params[0], params[1]);
        return response;
    }

    private String sendReviewCountRequest(String restaurantId, String dishId){
        try{
            URL url = new URL(String.format(SEND_REVIEW_COUNT_URL, restaurantId, dishId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);
            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while(null != (line = reader.readLine())){
                result.append(line).append("\n");
            }
            Log.d(TAG, "sendMenuSectionRequest: " + result);
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
        if(response == null){
            Log.e(TAG, "onPostExecute: Error downloading");
            return;
        }
        parseAndSendResponse(response);
    }

    public void parseAndSendResponse(String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String numReviews = jsonResponse.getString(RESPONSE_PARAM_NUMREVIEWS);
            if(textView != null){
                textView.setText(numReviews + " REVIEWS");
            }
        }catch(JSONException e){
            e.printStackTrace();
            Log.e(TAG, "parseAndSendResponse: Error proecessing json data " + e.getMessage());
        }
    }
}
