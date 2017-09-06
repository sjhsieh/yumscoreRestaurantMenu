package com.yumscore.yumscorerestaurantmenu;

import android.os.Build;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;

/**
 */
public class AWSManager {

    private static final String _ACCESS_KEY_ID = BuildConfig.AWS_ACCESS_KEY;
    private static final String _SECRET_KEY = BuildConfig.AWS_SECRET_KEY;

    private static AmazonSNSClient snsClient = null;

    //returns the instance of the amazon SNS Client for this app
    public static AmazonSNSClient getSNSClient(){
        if(snsClient == null){
            snsClient = new AmazonSNSClient( new BasicAWSCredentials( _ACCESS_KEY_ID, _SECRET_KEY ) );
            snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
        }
        return snsClient;
    }

}