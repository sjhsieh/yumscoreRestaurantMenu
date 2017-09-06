package com.yumscore.yumscorerestaurantmenu;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import java.io.File;

/**
 * Created by steve on 6/21/17.
 */

public class DeleteImages extends AsyncTask<String, Void, String> {
    private static final String TAG = "DeleteImages";

    Context context = null;

    public DeleteImages(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String[] params) {
        String response = deleteImages(params);
        return response;
    }

    private String deleteImages(String[] dishIds){
        if(context != null) {
            ContextWrapper cw = new ContextWrapper(context);
            File thumbnailDirectory = cw.getDir("dishThumbnails", Context.MODE_PRIVATE);
            File imageDirectory = cw.getDir("dishImages", Context.MODE_PRIVATE);
            for (int i = 0; i < dishIds.length; i++) {
                File thumbnailFile = new File(thumbnailDirectory, dishIds[i] + ".png");
                thumbnailFile.delete();
                File imageFile = new File(imageDirectory, dishIds[i] + ".png");
                imageFile.delete();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {

    }
}
