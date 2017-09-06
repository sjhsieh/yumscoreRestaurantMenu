package com.yumscore.yumscorerestaurantmenu;

import android.content.Context;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by steve on 6/21/17.
 */

public class DownloadImage extends AsyncTask<Dish, Void, String> {
    private static final String TAG = "DownloadImage";



    Context context = null;

    public DownloadImage(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(Dish[] params) {
        String response = downloadImage(params);
        return response;
    }

    private String downloadImage(Dish[] dishes){
        if(context != null){

        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {

    }
}
