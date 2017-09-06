package com.yumscore.yumscorerestaurantmenu;

import android.os.AsyncTask;
import android.util.Log;

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
 * Created by steve on 2/28/17.
 */

public class SendAndParseReviewsForDishRequest extends AsyncTask<String, Void, String> {
    private static final String TAG = "SendAndParseReviewsForD";

    private static final String GET_REVIEWS_FOR_DISH_URL = "http://raddish.yumscore.com/api/v1/getReviewsForDish?restaurantId=%s&dishId=%s";
    private static final String RESPONSE_PARAM_RESTAURANTNAME = "restaurantName";
    private static final String RESPONSE_PARAM_USERTOKEN = "usertoken";
    private static final String RESPONSE_PARAM_REVIEWS = "reviews";
    private static final String RESPONSE_PARAM_RESTAURANTID = "restaurantid";
    private static final String RESPONSE_PARAM_USERNAME = "username";
    private static final String RESPONSE_PARAM_REVIEWID = "reviewid";
    private static final String RESPONSE_PARAM_PROFILEPHOTOURL = "profilePhotoUrl";
    private static final String RESPONSE_PARAM_DISHNAME = "dishname";
    private static final String RESPONSE_PARAM_PHOTOURL = "photourl";
    private static final String RESPONSE_PARAM_PHOTOTHUMBNAILURL = "photothumbnailurl";
    private static final String RESPONSE_PARAM_REVIEWTEXT = "reviewtext";
    private static final String RESPONSE_PARAM_COMPLETED = "completed";
    private static final String RESPONSE_PARAM_RECOMMENDED = "recommended";
    private static final String RESPONSE_PARAM_FIRSTICON = "firsticon";
    private static final String RESPONSE_PARAM_SECONDICON = "secondicon";
    private static final String RESPONSE_PARAM_THIRDICON = "thirdicon";
    private static final String RESPONSE_PARAM_FOURTHICON = "fourthicon";
    private static final String RESPONSE_PARAM_FIFTHICON = "fifthicon";
    private static final String RESPONSE_PARAM_NUMVIEWS = "numViews";
    private static final String GET_REVIEWS_FOR_DISH_PARAMS = "&lastUsertoken=%s&lastCompleted=%s";

    private ReviewsForDishListener reviewsForDishListener;
    private boolean firstPage = true;

    interface ReviewsForDishListener{
        void onReviewsForDishesParsed(ArrayList<Review> reviews, boolean firstPage);
        void onReviewsForDishesFailed();
    }

    public SendAndParseReviewsForDishRequest(ReviewsForDishListener reviewsForDishListener) {
        this.reviewsForDishListener = reviewsForDishListener;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = null;
        if(params.length == 2){
            response = sendReviewsForDishRequest(params[0], params[1], null, null);
        }else {
            response = sendReviewsForDishRequest(params[0], params[1], params[2], params[3]);
        }
        return response;
    }

    private String sendReviewsForDishRequest(String restaurantId, String dishId, String lastUsertoken, String lastCompleted){
        try{
            String urlString = String.format(GET_REVIEWS_FOR_DISH_URL, restaurantId, dishId);
            if(lastUsertoken != null && lastCompleted != null){
                urlString = urlString + String.format(GET_REVIEWS_FOR_DISH_PARAMS, lastUsertoken, lastCompleted);
                firstPage = false;
            }
            URL url = new URL(urlString);
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
            Log.d(TAG, "sendReviewsForDishRequest: " + result);
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
            if(reviewsForDishListener != null){
                reviewsForDishListener.onReviewsForDishesFailed();
                return;
            }
        }
        parseAndSendResponse(response);
    }

    private void parseAndSendResponse(String response){
        try {
            ArrayList<Review> reviews = new ArrayList<>();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonReviews = jsonResponse.getJSONArray(RESPONSE_PARAM_REVIEWS);
            for(int i = 0; i < jsonReviews.length(); i++){
                JSONObject jsonReview = jsonReviews.getJSONObject(i);
                String restaurantName = jsonReview.getString(RESPONSE_PARAM_RESTAURANTNAME);
                String reviewUsertoken = jsonReview.getString(RESPONSE_PARAM_USERTOKEN);
                String reviewUsername = jsonReview.getString(RESPONSE_PARAM_USERNAME);
                String restaurantId = jsonReview.getString(RESPONSE_PARAM_RESTAURANTID);
                String reviewId = jsonReview.getString(RESPONSE_PARAM_REVIEWID);
                String photoThumbnailUrl = jsonReview.getString(RESPONSE_PARAM_PHOTOTHUMBNAILURL);
                String photoUrl = jsonReview.getString(RESPONSE_PARAM_PHOTOURL);
                String reviewProfileThumbnailUrl = jsonReview.getString(RESPONSE_PARAM_PROFILEPHOTOURL);
                String dishName = jsonReview.getString(RESPONSE_PARAM_DISHNAME);
                String reviewText = jsonReview.getString(RESPONSE_PARAM_REVIEWTEXT);
                Boolean recommended = jsonReview.getBoolean(RESPONSE_PARAM_RECOMMENDED);
                long completed = jsonReview.getLong(RESPONSE_PARAM_COMPLETED);
                String firstIcon = jsonReview.getString(RESPONSE_PARAM_FIRSTICON);
                String secondIcon = jsonReview.getString(RESPONSE_PARAM_SECONDICON);
                String thirdIcon = jsonReview.getString(RESPONSE_PARAM_THIRDICON);
                String fourthIcon = jsonReview.getString(RESPONSE_PARAM_FOURTHICON);
                String fifthIcon = jsonReview.getString(RESPONSE_PARAM_FIFTHICON);
                int numViews = jsonReview.getInt(RESPONSE_PARAM_NUMVIEWS);
                Review review = new Review(reviewUsertoken, reviewUsername, restaurantId, restaurantName, null, reviewId, photoThumbnailUrl,
                        photoUrl, reviewProfileThumbnailUrl, dishName, reviewText, recommended, completed, firstIcon, secondIcon, thirdIcon, fourthIcon, fifthIcon, numViews);
                reviews.add(review);
            }
            if(reviewsForDishListener != null){
                reviewsForDishListener.onReviewsForDishesParsed(reviews, firstPage);
            }
        }catch(JSONException e){
            e.printStackTrace();
            Log.e(TAG, "parseAndSendResponse: Error proecessing json data " + e.getMessage());
            if(reviewsForDishListener != null){
                reviewsForDishListener.onReviewsForDishesFailed();
            }
        }
    }
}
