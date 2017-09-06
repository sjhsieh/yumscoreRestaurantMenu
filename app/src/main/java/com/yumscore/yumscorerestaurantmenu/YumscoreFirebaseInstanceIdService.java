package com.yumscore.yumscorerestaurantmenu;

import android.util.Log;

import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by steve on 6/13/17.
 */

public class YumscoreFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "YumscoreFirebaseInstanc";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    public void sendRegistrationToServer(String refreshedToken){
        AWSManager.getSNSClient().createPlatformEndpoint(new CreatePlatformEndpointRequest()
                .withPlatformApplicationArn("arn:aws:sns:us-west-2:483592860808:app/GCM/Yumscore_Menu")
                .withToken(refreshedToken));
    }
}
