package com.yumscore.yumscorerestaurantmenu;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by steve on 3/3/17.
 */

public class DateTool {
    private static final String TAG = "DateTool";

    public static String getDateString(Long milliseconds){
        Date currentTime = new Date();
        long difference = currentTime.getTime() - milliseconds.longValue();
        Log.d(TAG, "getDateString: " + difference);
        if(difference < 60000){
            return Math.round(difference/1000L)+"s";
        }else if(difference < 3600000){
            return Math.round(difference/60000L)+"m";
        }else if(difference < 86400000){
            return Math.round(difference/3600000L)+"h";
        }else if(difference < 604800000){
            return Math.round(difference/86400000L)+"d";
        }else{
            TimeZone timeZone = TimeZone.getDefault();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            sdf.setTimeZone(timeZone);
            return sdf.format(new Date(milliseconds));
        }
    }
}
